package com.esticharalegal.backendServer.dto;


import com.esticharalegal.backendServer.Enum.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.security.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LawyerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    private String firstName;
    private String lastName;
    private String username;
    private String token;
    private String email;
    private String licenseNumber;
    private String phoneNumber;
    private String address;
    private String bio;
    private String profileImage;
    @JsonIgnore
    @Transient
    private KeyPair keyPair;

    @Lob // Use @Lob annotation to store large binary data (keys)
    private byte[] publicKey; // Store public key as byte array

    @Lob
    private byte[] privateKey;



    private void generateKeyPair() {
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
