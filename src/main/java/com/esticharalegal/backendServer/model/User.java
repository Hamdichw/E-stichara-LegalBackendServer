package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    @Column(name = "firstName", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(name = "Username")
    private String username;

    @Column(name = "Email")
    private String email;

    @Column(nullable = false)
    @Size(max = 100)
    private String password;

    @Column(name = "UserType")
    private String userType;


}
