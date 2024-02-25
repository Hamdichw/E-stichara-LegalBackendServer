package com.esticharalegal.backendServer.model;


import jakarta.persistence.*;

@Entity
@Table(name = "lawyers")
public class Lawyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lawyerID;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @Column(name = "Bio")
    private String bio;

    // Getters and setters
}
