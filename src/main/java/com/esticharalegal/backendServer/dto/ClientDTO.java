package com.esticharalegal.backendServer.dto;

import com.esticharalegal.backendServer.Enum.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.jfr.Frequency;
import lombok.*;

import java.security.*;


@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long userID;

    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String token;
    @Setter
    private UserType userType;
    @Getter
    @Setter
    private String bio;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
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
