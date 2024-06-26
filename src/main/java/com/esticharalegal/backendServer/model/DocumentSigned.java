package com.esticharalegal.backendServer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "documentsigned")
public class DocumentSigned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signID;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DocumentID")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "SignerID")
    private User signer;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String signature;

    @Column(name = "SignedDate")
    private LocalDateTime signedDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] content;
    // Getters and setters
}
