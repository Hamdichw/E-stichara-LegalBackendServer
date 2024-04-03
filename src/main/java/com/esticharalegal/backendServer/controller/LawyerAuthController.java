package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.config.UserAuthenticationProvider;
import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.service.LawyerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/LawyerAuth")
public class LawyerAuthController {
    private final LawyerService lawyerService;
    private  final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<LawyerDTO> login(@RequestBody @Valid CredentialsLawyerDTO credentialsLawyerDTO, HttpServletResponse response) throws AppException {
        LawyerDTO lawyerDto = lawyerService.login(credentialsLawyerDTO);
        lawyerDto.setToken(userAuthenticationProvider.createToken(lawyerDto));
        Cookie cookie = new Cookie("token", lawyerDto.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(3600); // set cookie expiration time in seconds, adjust as needed
        response.addCookie(cookie);
        return ResponseEntity.ok(lawyerDto);
    }

    @PostMapping("/register")
    public ResponseEntity<LawyerDTO> register(@RequestBody @Valid SignUpLawyerDTO user) throws AppException {
        LawyerDTO createdUser = lawyerService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        createdUser.setUserType(UserType.LAWYER);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getUserID())).body(createdUser);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null); // Setting the token to null effectively removes it
        cookie.setPath("/");
        cookie.setMaxAge(0); // Immediately expire the cookie
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/{userId}/connections/{connectionUserId}")
    public ResponseEntity<String> addConnection(@PathVariable Long userId, @PathVariable Long connectionUserId) {
        lawyerService.addConnection(userId, connectionUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Connection added successfully");
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestParam("email") String email) {
        lawyerService.generateResetPasswordToken(email);
    }



}
