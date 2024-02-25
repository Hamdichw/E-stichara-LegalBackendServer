package com.esticharalegal.backendServer.model;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int documentID;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "UploadedBy")
    private User uploadedBy;

    @Column(name = "UploadDate")
    private Date uploadDate;

    @Column(name = "FileURL")
    private String fileURL;

    // Getters and setters
}
