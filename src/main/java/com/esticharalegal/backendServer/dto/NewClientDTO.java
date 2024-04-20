package com.esticharalegal.backendServer.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NewClientDTO {


    private String firstName;
    private String lastName;
    private String username;
    private String address;
    private String phoneNumber;
    private String email;
    private String bio;

}