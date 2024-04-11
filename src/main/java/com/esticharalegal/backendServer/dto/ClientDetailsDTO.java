package com.esticharalegal.backendServer.dto;

import com.esticharalegal.backendServer.Enum.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.security.*;
import java.util.Date;
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ClientDetailsDTO {
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
    private String address;

    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String profileImage;
    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Getter
    @Setter
    private String password;
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