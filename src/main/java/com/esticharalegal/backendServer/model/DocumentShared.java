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
