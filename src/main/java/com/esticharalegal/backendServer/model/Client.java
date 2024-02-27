package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clientID;

    @OneToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "ContactNumber")
    private String contactNumber;

    @Column(name = "Address")
    private String address;

    // Getters and setters
}
