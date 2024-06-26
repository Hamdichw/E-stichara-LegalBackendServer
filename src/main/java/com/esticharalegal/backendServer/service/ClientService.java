package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.dto.ClientDTO;
import com.esticharalegal.backendServer.dto.CredentialsClientDTO;
import com.esticharalegal.backendServer.dto.CredentialsGoogle;
import com.esticharalegal.backendServer.dto.SignUpClientDTO;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.mapper.UserMapper;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ClientService {

    private  final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    private  final UserMapper clientMapper;

    private final CloudService cloudService;

    public ClientDTO loginGoogle(CredentialsGoogle credentialsGoogle)throws  AppException{
        Optional<User> user = userRepository.findByEmail(credentialsGoogle.email());
        if(user.isPresent()){
            return clientMapper.toClientDto(user.get());
        }else{
            User newUser = clientMapper.credentialsGoogleToUser(credentialsGoogle);
            newUser.generateKeyPair();
            newUser.setRole(UserType.CLIENT);
            newUser.setPassword("GoogleUser");
            User savedUser = userRepository.save(newUser);
            return clientMapper.toClientDto(savedUser);
        }

    }


    public ClientDTO login(CredentialsClientDTO credentialsClientDto) throws AppException {
        User user = userRepository.findByEmail(credentialsClientDto.email())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsClientDto.password()), user.getPassword())) {
            return clientMapper.toClientDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public ClientDTO register(SignUpClientDTO userDto) throws AppException {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.email());
        Optional<User> userNameExist = userRepository.findByUsername(userDto.username());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = clientMapper.signUpToUser(userDto);
        if(userNameExist.isPresent()){
            String newUsername = userDto.username() + userNameExist.get().getUserID() + 1;
            user.setUsername(newUsername);
        }
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));
        user.generateKeyPair();
        user.setRole(UserType.CLIENT);
        User savedUser = userRepository.save(user);

        return clientMapper.toClientDto(savedUser);
    }

    public ClientDTO findByLogin(String login) throws AppException {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return clientMapper.toClientDto(user);
    }
    // Method to generate reset password token and send it to the user
    public void generateResetPasswordToken(String email) throws AppException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {

            String newPassword = generateRandomPassword();


            String encodedPassword = passwordEncoder.encode(newPassword);
            user.get().setPassword(encodedPassword);


            userRepository.save(user.get());


            sendNewPasswordByEmail(user.get().getEmail(), newPassword);
            throw new AppException("password updated",HttpStatus.OK);
        }else{
            throw new AppException("Invalid mail",HttpStatus.OK);

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
    public String generateCodeVerification(String email) {
        String CodeVerification = generateRandomPassword();
        sendCodeVerification(email, CodeVerification);
        return  CodeVerification;
    }
    private void sendCodeVerification(String email, String newPassword) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your Code Verification");
        mailMessage.setText("Your Code Verification: " + newPassword);

        javaMailSender.send(mailMessage);


    }

    // Method to update the profile image
    public String updateProfileImage(Long userId, MultipartFile image) throws AppException {
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
            return  imageUrl;
        } else {
            // Handle case where user is not found
            throw new AppException("User not found", HttpStatus.NOT_FOUND);
        }
    }


    public ClientDTO updateUser(long userId, User updatedUser) throws AppException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found with id: " + userId,HttpStatus.BAD_REQUEST));
        if(updatedUser.getPassword() != null){
            updatedUser.setPassword(passwordEncoder.encode(CharBuffer.wrap(updatedUser.getPassword())));
            BeanUtils.copyProperties(updatedUser, existingUser, "userID" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role");

        }else{
            BeanUtils.copyProperties(updatedUser, existingUser, "userID","password" ,"connections", "keyPair", "publicKey", "privateKey","profileImage","role");

        }


        // Save and return updated user
        User savedUser =  userRepository.save(existingUser);
        return clientMapper.toClientDto(savedUser);

    }
}


