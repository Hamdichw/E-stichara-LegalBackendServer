package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.exceptions.*;

import com.esticharalegal.backendServer.model.Chat;
import com.esticharalegal.backendServer.model.Message;
import com.esticharalegal.backendServer.repository.ChatRepository;
import com.esticharalegal.backendServer.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
@Service
public class ChatService {
    
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Chat addChat(Chat chat) {
        return chatRepository.save(chat);
    }

     
    public Message addMessage2(Message message) {
        return messageRepository.save(message);
    }

     
    public List<Message> getAllMessagesInChat(int chatId) throws AppException {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if(chat.isEmpty()){
            throw new AppException();
        }else {
            return chat.get().getMessageList();
        }
    }

     
    public List<Chat> findallchats() throws AppException {
        if (chatRepository.findAll().isEmpty()) {
            throw new AppException();
        } else {
            return chatRepository.findAll();
        }

    }

     
    public Chat getById(int id) throws AppException {
        Optional<Chat> chatid = chatRepository.findById(id);
        if (chatid.isPresent()) {
            return chatid.get();
        } else {
            throw new AppException();
        }
    }

     
    public HashSet<Chat> getChatByFirstUserName(String username) throws AppException {
        HashSet<Chat> chat = chatRepository.getChatByFirstUserName(username);

        if (chat.isEmpty()) {
            throw new AppException();
        } else {
            return chat;
        }
    }

     
    public HashSet<Chat> getChatBySecondUserName(String username) throws AppException {
        HashSet<Chat> chat = chatRepository.getChatBySecondUserName(username);
        if (chat.isEmpty()) {
            throw new AppException();
        } else {
            return chat;
        }
    }

     
    public HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username) throws AppException {
        HashSet<Chat> chat = chatRepository.getChatByFirstUserName(username);
        HashSet<Chat> chat1 = chatRepository.getChatBySecondUserName(username);

        chat1.addAll(chat);

        if (chat.isEmpty() && chat1.isEmpty()) {
            throw new AppException();
        } else if (chat1.isEmpty()) {
            return chat;
        } else {
            return chat1;
        }
    }

     
    public HashSet<Chat> getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName) throws AppException {
        HashSet<Chat> chat = chatRepository.getChatByFirstUserNameAndSecondUserName(firstUserName, secondUserName);
        HashSet<Chat> chat1 = chatRepository.getChatBySecondUserNameAndFirstUserName(firstUserName, secondUserName);
        if (chat.isEmpty() && chat1.isEmpty()) {
            throw new AppException();
        } else if (chat.isEmpty()) {
            return chat1;
        } else {
            return chat;
        }
    }

     
    public Chat addMessage(Message add, int chatId) throws AppException {
        Optional<Chat> chat=chatRepository.findById(chatId);
        Chat abc=chat.get();

        if(abc.getMessageList()==null){
            List<Message> msg=new ArrayList<>();
            msg.add(add);
            abc.setMessageList(msg);
            return chatRepository.save(abc);
        }else{
            List<Message> rates=abc.getMessageList();
            rates.add(add);
            abc.setMessageList(rates);
            return chatRepository.save(abc);
        }
    }



}
