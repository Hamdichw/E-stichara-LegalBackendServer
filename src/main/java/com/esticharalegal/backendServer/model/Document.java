package com.esticharalegal.backendServer.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentID;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "UploadedBy")
    private User uploadedBy;

    @Column(name = "UploadDate")
    private LocalDateTime uploadDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] content;


    // Getters and setters
}
