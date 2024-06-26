package com.esticharalegal.backendServer.service;


import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.mapper.UserMapper;
import com.esticharalegal.backendServer.model.Chat;
import com.esticharalegal.backendServer.model.User;

import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.repository.ChatRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.CharBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LawyerService {

    private  final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    private  final UserMapper lawyerMapper;

    private final CloudService cloudService;

    private  final AppointmentService appointmentService;

    private final ChatRepository chatRepository;
    public LawyerDTO login(CredentialsLawyerDTO credentialsLawyerDTO) throws AppException {
        User user = userRepository.findByLicenseNumberAndEmail(credentialsLawyerDTO.licenseNumber() , credentialsLawyerDTO.email())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsLawyerDTO.password()), user.getPassword())) {
            return lawyerMapper.toLawyerDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public LawyerDTO register(SignUpLawyerDTO userDto) throws AppException {
        Optional<User> optionalUser = userRepository.findByLicenseNumber(userDto.licenseNumber());
        Optional<User> userNameExist = userRepository.findByUsername(userDto.username());

        if (optionalUser.isPresent()) {
            throw new AppException("Licence Number already exists", HttpStatus.BAD_REQUEST);
        }

        User user = lawyerMapper.signUpToUser(userDto);
        if(userNameExist.isPresent()){
            String newUsername = userDto.username() + userNameExist.get().getUserID() + 1;
            user.setUsername(newUsername);
        }
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));
        user.generateKeyPair();
        user.setRole(UserType.LAWYER);
        user.setAddress("Tunis");
        User savedUser = userRepository.save(user);

        return lawyerMapper.toLawyerDto(savedUser);
    }

    public LawyerDTO findByLogin(String login) throws AppException {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return lawyerMapper.toLawyerDto(user);
    }
    public void addConnection(Long userId, Long connectionUserId, Long appointmentId) throws AppException {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> connectionUser = userRepository.findById(connectionUserId);

        if (user.isPresent() && connectionUser.isPresent()) {
            User userEntity = user.get();
            User connectionUserEntity = connectionUser.get();

            // Check if connectionUserEntity already exists in userEntity's connections
            if (!userEntity.getConnections().contains(connectionUserEntity)) {
                userEntity.getConnections().add(connectionUserEntity);
                connectionUserEntity.getConnections().add(userEntity);
                userRepository.save(userEntity);
                userRepository.save(connectionUserEntity);
                appointmentService.acceptAppointment(appointmentId);
                Chat chat =  new Chat();
                chat.setFirstUser(userEntity);
                chat.setSecondUser(connectionUserEntity);
                chatRepository.save(chat);
                 throw new AppException("Connection added successfully , Appointment added successfully", HttpStatus.CREATED);
            } else {
                appointmentService.acceptAppointment(appointmentId);
                throw new AppException(" Appointment added successfully",  HttpStatus.CREATED);
            }
        } else {
            throw new AppException("One or both users not found", HttpStatus.NOT_FOUND);
        }
    }


    public void  updateClient(Long userId , NewClientDTO newClientDTO) throws AppException {
        Optional<User> client =  userRepository.findById(userId);
        User updateClient = lawyerMapper.NewClientToUser(newClientDTO);
        User currentUser = client.get();
        BeanUtils.copyProperties(updateClient, currentUser, "userID","username","password" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role");
        User savedUser =  userRepository.save(currentUser);
        throw  new AppException("client updated",HttpStatus.OK);
    }
    public void addClient(Long userId, NewClientDTO connectionClient) throws AppException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }

        Optional<User> existEmail = userRepository.findByEmail(connectionClient.email());
        if(existEmail.isPresent()){
            throw new AppException("Client with this email already exists", HttpStatus.CONFLICT);
        }

        Optional<User> connectionUser = userRepository.findByUsername(connectionClient.username());
        User connectionUserEntity = lawyerMapper.NewClientToUser(connectionClient);
        if(connectionUser.isPresent()){
            String modifiedUsername = connectionClient.username() + connectionUser.get().getUserID() + 1;
            connectionUserEntity.setUsername(modifiedUsername);

        }


        connectionUserEntity.setRole(UserType.CLIENT);
        User client = userRepository.save(connectionUserEntity);
        // Add connections
       User userEntity = user.get();
        userEntity.getConnections().add(client);


       // Save only if everything is successful
        userRepository.save(userEntity);
    }
    public List<ClientDetailsDTO> getAllConnectionsByLawyerId(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            List<User> connections = user.get().getConnections();
            return connections.stream()
                    .map(lawyerMapper::toClientDetailsDTO)
                    .collect(Collectors.toList());
        }
        return null;
    }


    // Method to generate reset password token and send it to the user
    public void generateResetPasswordToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user != null) {

            String newPassword = generateRandomPassword();


            String encodedPassword = passwordEncoder.encode(newPassword);
            user.get().setPassword(encodedPassword);


            userRepository.save(user.get());


            sendNewPasswordByEmail(user.get().getEmail(), newPassword);
        }
    }
    // Method to generate a random password
    private String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()_+";

        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

        StringBuilder newPassword = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) { // Generate a 10-character password
            newPassword.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return newPassword.toString();
    }

    // Method to send the new password to the user via email
    private void sendNewPasswordByEmail(String email, String newPassword) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your new password");
        mailMessage.setText("Your new password is: " + newPassword);

        javaMailSender.send(mailMessage);
    }


    //send Code Verification
    public void generateCodeVerification(String email) throws AppException {
            String CodeVerification = generateRandomPassword();
            sendNewPasswordByEmail(email, CodeVerification);
            throw new AppException(CodeVerification,HttpStatus.OK);
    }

    public void updateProfileImage(Long userId, MultipartFile image) throws AppException {
        // Retrieve the user from the repository
        Optional<User> optionalUser = userRepository.findById(userId);
        if(image.isEmpty()){
            throw new AppException("Empty file",HttpStatus.BAD_REQUEST);
        }
        if (optionalUser.isPresent()) {
            // Upload the image to Cloudinary using CloudService
            String imageUrl = cloudService.uploadFile(image);
            // Update the user's profile image URL
            User user = optionalUser.get();
            user.setProfileImage(imageUrl);
            userRepository.save(user);
            throw new AppException(imageUrl, HttpStatus.OK);

        } else {
            // Handle case where user is not found
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
    }
    public LawyerDTO updateUser(long userId, User updatedUser) throws AppException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found with id: " + userId,HttpStatus.BAD_REQUEST));
        if(updatedUser.getPassword() != null){
            updatedUser.setPassword(passwordEncoder.encode(CharBuffer.wrap(updatedUser.getPassword())));
            BeanUtils.copyProperties(updatedUser, existingUser, "userID" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role","username");

        }else {
            BeanUtils.copyProperties(updatedUser, existingUser, "userID","password" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role", "username");

        }


        // Save and return updated user
        User savedUser =  userRepository.save(existingUser);
        return lawyerMapper.toLawyerDto(savedUser);

    }

}
