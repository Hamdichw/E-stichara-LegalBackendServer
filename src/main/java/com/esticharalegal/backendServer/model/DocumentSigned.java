package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "documentsigned")
public class DocumentSigned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int signID;

    @ManyToOne
    @JoinColumn(name = "DocumentID")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "SignerID")
    private User signer;

    @Column(name = "Signature")
    private String signature;

    @Column(name = "SignedDate")
    private Date signedDate;

    // Getters and setters
}
