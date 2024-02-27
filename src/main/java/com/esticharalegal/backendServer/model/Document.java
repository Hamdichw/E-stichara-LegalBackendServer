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
