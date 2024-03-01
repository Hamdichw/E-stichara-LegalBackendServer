package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.dto.CredentialsDTO;
import com.esticharalegal.backendServer.dto.SignUpDTO;
import com.esticharalegal.backendServer.dto.UserDTO;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.mapper.UserMapper;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class UserService {

    private  final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private  final UserMapper userMapper;

    public UserDTO login(CredentialsDTO credentialsDto) throws AppException {
        User user = userRepository.findByUsername(credentialsDto.username())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDTO register(SignUpDTO userDto) throws AppException {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.username());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDTO findByLogin(String login) throws AppException {
        User user = userRepository.findByUsername(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

}
