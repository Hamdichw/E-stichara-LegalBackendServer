package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;
import org.hibernate.usertype.UserType;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(name = "Username")
    private String username;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "UserType")
    private String userType;

    // Getters and setters
}
