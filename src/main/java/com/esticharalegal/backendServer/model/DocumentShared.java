package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "documentshared")
public class DocumentShared {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shareID;

    @ManyToOne
    @JoinColumn(name = "DocumentID")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "SharedWith")
    private User sharedWith;

    @Column(name = "SharedDate")
    private Date sharedDate;

    // Getters and setters
}
