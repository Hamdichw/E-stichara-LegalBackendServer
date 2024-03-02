package com.esticharalegal.backendServer.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name="chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int chatId;

    private String firstUserName;
    private String secondUserName;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messageList;



}
