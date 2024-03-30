package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.mapper.UserMapper;
import com.esticharalegal.backendServer.model.User;

import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LawyerService {

    private  final UserRepository userRepository;


    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    private  final UserMapper lawyerMapper;

    public LawyerDTO login(CredentialsLawyerDTO credentialsLawyerDTO) throws AppException {
        User user = userRepository.findByUsername(credentialsLawyerDTO.username())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsLawyerDTO.password()), user.getPassword())) {
            return lawyerMapper.toLawyerDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public LawyerDTO register(SignUpLawyerDTO userDto) throws AppException {
        Optional<User> optionalUser = userRepository.findByLicenseNumber(userDto.licenseNumber());
        Optional<User> userNameExist = userRepository.findByUsername(userDto.username());
        if(userNameExist.isPresent()){
            throw new AppException("user Name already exists", HttpStatus.BAD_REQUEST);
        }
        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = lawyerMapper.signUpToUser(userDto);
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
    public void addConnection(Long userId, Long connectionUserId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<User> connectionUser = userRepository.findById(connectionUserId);
        if (user.isPresent() && connectionUser.isPresent()) {
            User userEntity = user.get();
            User connectionUserEntity = connectionUser.get();
            userEntity.getConnections().add(connectionUserEntity);
            connectionUserEntity.getConnections().add(userEntity);
            userRepository.save(userEntity);
            userRepository.save(connectionUserEntity);
        }
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
}
