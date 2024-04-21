package com.esticharalegal.backendServer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    @Column(name = "UploadedByUserName")
    private String UploadedByUserName ;

    @Column(name = "UploadDate")
    private LocalDateTime uploadDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] content;


    @JsonIgnore
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL) // Cascade delete behavior
    private List<DocumentShared> documentSharedList; // List of DocumentShared entities


    @JsonIgnore
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL) // Cascade delete behavior
    private List<DocumentSigned> documentSignedList; // List of DocumentSigned entities
}
