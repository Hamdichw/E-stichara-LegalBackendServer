package com.esticharalegal.backendServer.dto;

import com.esticharalegal.backendServer.Enum.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.security.*;
import java.util.Date;
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientDetailsDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String username;


    @Setter
    private String address;

    @Setter
    private String phoneNumber;
    @Setter
    private String email;
    @Setter
    private String profileImage;
    @Setter
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Setter
    private boolean password;

}