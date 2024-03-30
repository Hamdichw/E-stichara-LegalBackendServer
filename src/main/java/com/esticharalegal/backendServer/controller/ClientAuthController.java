package com.esticharalegal.backendServer.controller;


import com.esticharalegal.backendServer.config.UserAuthenticationProvider;
import com.esticharalegal.backendServer.dto.ClientDTO;
import com.esticharalegal.backendServer.dto.CredentialsClientDTO;
import com.esticharalegal.backendServer.dto.SignUpClientDTO;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.service.ClientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/ClientAuth")
public class ClientAuthController {

    private final ClientService clientService;
    private  final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<ClientDTO> login(@RequestBody @Valid CredentialsClientDTO credentialsClientDto, HttpServletResponse response) throws AppException {
        ClientDTO clientDto = clientService.login(credentialsClientDto);
        clientDto.setToken(userAuthenticationProvider.createToken(clientDto));
        clientDto.setUserType(UserType.LAWYER);
        Cookie cookie = new Cookie("token", clientDto.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(3600); // set cookie expiration time in seconds, adjust as needed
        response.addCookie(cookie);
        return ResponseEntity.ok(clientDto);
    }

    @PostMapping("/register")
    public ResponseEntity<ClientDTO> register(@RequestBody @Valid SignUpClientDTO user) throws AppException {
        ClientDTO createdUser = clientService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        createdUser.setUserType(UserType.CLIENT);
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
    @PostMapping("/reset")
    public void resetPassword(@RequestParam("email") String email) {
        clientService.generateResetPasswordToken(email);
    }
}