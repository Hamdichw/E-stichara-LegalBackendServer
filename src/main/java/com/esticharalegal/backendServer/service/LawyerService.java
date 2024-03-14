package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.mapper.UserMapper;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.model.UserType;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;
@Service
@AllArgsConstructor
public class LawyerService {

    private  final UserRepository userRepository;


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
}
