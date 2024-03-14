package com.esticharalegal.backendServer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    @Column(name = "firstName", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(name = "Username")
    private String username;

    @Column(name = "Email")
    private String email;

    @Column(nullable = false)
    @Size(max = 100)
    private String password;

    @Column(name = "UserType")
    private UserType role;

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @Column(name = "Bio")
    private String bio;
    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connected_user_id")
    )
    private List<User> connections;
    @Transient
    private KeyPair keyPair;

    @Lob // Use @Lob annotation to store large binary data (keys)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] publicKey; // Store public key as byte array

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] privateKey;
    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair pair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();

            this.publicKey = publicKey.getEncoded();
            this.privateKey = privateKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
}
