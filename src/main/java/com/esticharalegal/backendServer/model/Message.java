package com.esticharalegal.backendServer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ManyToAny;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;
    @Getter
    @Setter
    private String senderName;
    @Getter
    @Setter
    private Date time = new Date(System.currentTimeMillis());
    @Getter
    @Setter
    private String replymessage;


    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat;


}
