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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatId;


    @ManyToOne
    private User firstUser;


    @ManyToOne
    private User secondUser;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messageList;





}
