package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.exceptions.*;

import com.esticharalegal.backendServer.model.Chat;
import com.esticharalegal.backendServer.model.Message;

import java.util.HashSet;
import java.util.List;

public interface ChatService {

    public Chat addChat(Chat chat) throws ChatAlreadyExistException;

    List<Chat> findallchats() throws NoChatExistsInTheRepository;

    Chat getById(int id)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatBySecondUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username)  throws ChatNotFoundException;

    HashSet<Chat> getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName)  throws ChatNotFoundException;

    Chat addMessage(Message add, int chatId)  throws ChatNotFoundException;


    Message addMessage2(Message message);

    List<Message> getAllMessagesInChat(int chatId) throws NoChatExistsInTheRepository;
}
