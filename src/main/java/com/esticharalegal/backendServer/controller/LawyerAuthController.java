package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.config.UserAuthenticationProvider;
import com.esticharalegal.backendServer.dto.*;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.User;
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

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/LawyerAuth")
public class LawyerAuthController {
    private final LawyerService lawyerService;
    private  final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<LawyerDTO> login(@RequestBody @Valid CredentialsLawyerDTO credentialsLawyerDTO, HttpServletResponse response) throws AppException {
        LawyerDTO lawyerDto = lawyerService.login(credentialsLawyerDTO);
        lawyerDto.setToken(userAuthenticationProvider.createToken(lawyerDto));

        return ResponseEntity.ok(lawyerDto);
    }

    @PostMapping("/register")
    public ResponseEntity<LawyerDTO> register(@RequestBody @Valid SignUpLawyerDTO user) throws AppException {
        LawyerDTO createdUser = lawyerService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
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



    @PostMapping("/{userId}/connections/{connectionUserId}/appointments/{appointmentId}")
    public void addConnection(@PathVariable Long userId, @PathVariable Long connectionUserId, @PathVariable Long appointmentId) throws AppException {
        lawyerService.addConnection(userId, connectionUserId,appointmentId);
    }
    @PostMapping("/{userId}/client")
    public void addClient(@PathVariable Long userId, @RequestBody @Valid NewClientDTO clientDetailsDTO) throws AppException {
        lawyerService.addClient(userId, clientDetailsDTO);
    }
    @PutMapping("/{userId}/updateClient")
    public void updateClient(@PathVariable Long userId, @RequestBody @Valid NewClientDTO clientDetailsDTO) throws AppException {
        lawyerService.updateClient(userId, clientDetailsDTO);
    }
    @GetMapping("/{userId}/connections")
    public ResponseEntity<List<ClientDetailsDTO>> getAllConnectionsByUserId(@PathVariable("userId") long userId) {
        List<ClientDetailsDTO> connections = lawyerService.getAllConnectionsByLawyerId(userId);
        if (connections != null) {
            return new ResponseEntity<>(connections, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    };


    @PostMapping("/reset")
    public void resetPassword(@RequestParam("email") String email) {
        lawyerService.generateResetPasswordToken(email);
    }

    @PostMapping("/{userId}/profile-image")
    public void updateProfileImage(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile image) throws AppException {

        lawyerService.updateProfileImage(userId, image);

    }

    @PutMapping("/{userId}")
    public ResponseEntity<LawyerDTO> updateUser(@PathVariable long userId, @RequestBody User updatedUser) throws AppException {
        return  ResponseEntity.ok(lawyerService.updateUser(userId, updatedUser));
    }
}
