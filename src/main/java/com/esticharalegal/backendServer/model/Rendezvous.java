package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "rendezvous")
public class Rendezvous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentID;

    @ManyToOne
    @JoinColumn(name = "ClientID")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "LawyerID")
    private Lawyer lawyer;

    @Column(name = "Date")
    private Date date;

    @Column(name = "Time")
    private Date time;

    @Column(name = "Status")
    private String status;

    // Getters and setters
}
