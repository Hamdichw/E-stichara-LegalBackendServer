package com.esticharalegal.backendServer.model;

import com.esticharalegal.backendServer.Enum.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.security.*;
import java.util.Date;
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

    @Column(name = "firstName")
    @Size(max = 100)
    private String firstName;

    @Column(name = "lastName")
    @Size(max = 100)
    private String lastName;

    @Column(name = "Username")
    private String username;


    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "Email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "PhoneNumber")
    @Size(max = 8)
    private String phoneNumber;

    @Size(max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "UserType")
    private UserType role = UserType.CLIENT;



    @Column(name = "LicenseNumber")
    private String licenseNumber;

    @Column(name = "Bio/Accusation")
    private String bio;

    @Column(name = "Birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
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
