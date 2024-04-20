package com.esticharalegal.backendServer.service;


import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.mapper.UserMapper;
import com.esticharalegal.backendServer.model.User;

import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.repository.UserRepository;
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
        User savedUser = userRepository.save(user);

        return lawyerMapper.toLawyerDto(savedUser);
    }

    public LawyerDTO findByLogin(String login) throws AppException {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return lawyerMapper.toLawyerDto(user);
    }
    public void addConnection(Long userId, Long connectionUserId) throws AppException {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> connectionUser = userRepository.findById(connectionUserId);
        if (user.isPresent() && connectionUser.isPresent()) {
            User userEntity = user.get();
            User connectionUserEntity = connectionUser.get();
            userEntity.getConnections().add(connectionUserEntity);
            connectionUserEntity.getConnections().add(userEntity);
            userRepository.save(userEntity);
            userRepository.save(connectionUserEntity);
         throw new AppException("Connection added successfully", HttpStatus.CREATED);
        }
    }
    public void addClient(Long userId, ClientDetailsDTO connectionClient) throws AppException {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> connectionUser = userRepository.findByUsername(connectionClient.getUsername());
        Optional<User> existEmail = userRepository.findByEmail(connectionClient.getEmail());
        if(existEmail.isPresent()){
            throw  new AppException("Client already exist" , HttpStatus.OK);
        }
        if(connectionUser.isPresent()){
            connectionClient.setUsername(connectionClient.getUsername() + connectionUser.get().getUserID() + 1);

        }
        if (user.isPresent()){
            User userEntity = user.get();
            User connectionUserEntity = lawyerMapper.clientDetailsDTOToUser(connectionClient);
            userEntity.getConnections().add(connectionUserEntity);
            connectionUserEntity.getConnections().add(userEntity);
            userRepository.save(userEntity);
            userRepository.save(connectionUserEntity);
            throw  new AppException("Client added " , HttpStatus.OK);

        }

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
    public void generateCodeVerification(String email) {
            String CodeVerification = generateRandomPassword();
            sendNewPasswordByEmail(email, CodeVerification);
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
            BeanUtils.copyProperties(updatedUser, existingUser, "userID" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role");

        }else {
            BeanUtils.copyProperties(updatedUser, existingUser, "userID","password" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role");

        }


        // Save and return updated user
        User savedUser =  userRepository.save(existingUser);
        return lawyerMapper.toLawyerDto(savedUser);

    }

}
