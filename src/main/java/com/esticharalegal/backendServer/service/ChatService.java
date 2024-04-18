package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.exceptions.*;

import com.esticharalegal.backendServer.model.Chat;
import com.esticharalegal.backendServer.model.Message;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.ChatRepository;
import com.esticharalegal.backendServer.repository.MessageRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    public String addChat(Chat chat) throws AppException {
        Optional<User> firstuser = userRepository.findById(chat.getFirstUser().getUserID());
        Optional<User> seconduser = userRepository.findById(chat.getSecondUser().getUserID());
        if(firstuser.isPresent() && seconduser.isPresent()){
            chat.setFirstUser(firstuser.get());
            chat.setSecondUser(seconduser.get());
             chatRepository.save(chat);
            return "chat created" ;
        }
        else {
            throw  new  AppException("user not exist ",HttpStatus.BAD_REQUEST);
        }
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
    public Message getLastMessageInChat(int chatId) throws AppException {
        Optional<Chat> chat = chatRepository.findById(chatId);

        if (chat.isEmpty()) {
            throw new AppException();
        } else {
            List<Message> messageList = chat.get().getMessageList();
            if (!messageList.isEmpty()) {
                return messageList.get(messageList.size() - 1); // Get the last message
            } else {
                // If the message list is empty, return null or handle as per your requirements
                return null;
            }
        }
    }


    public HashSet<Chat> findallchatsClients(Long userId) throws AppException {
        Optional<User> seconduser = userRepository.findById(userId);
        if (chatRepository.getChatBySecondUser(seconduser.get()).isEmpty()) {
            throw new AppException();
        } else {
            return chatRepository.getChatBySecondUser(seconduser.get());
        }

    }
    public HashSet<Chat> findallchatsLawyers(Long userId) throws AppException {
        Optional<User> firstUser = userRepository.findById(userId);
        if (chatRepository.getChatByFirstUser(firstUser.get()).isEmpty()) {
            throw new AppException();
        } else {
            return chatRepository.getChatByFirstUser(firstUser.get());
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
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            HashSet<Chat> chat = chatRepository.getChatByFirstUser(user.get());

            if (chat.isEmpty()) {
                throw new AppException("empty chat",HttpStatus.BAD_REQUEST);
            } else {
                return chat;
            }
        }else{
            throw new AppException("user doesn't exist", HttpStatus.BAD_REQUEST);
        }

    }


    public HashSet<Chat> getChatBySecondUserName(String username) throws AppException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            HashSet<Chat> chat = chatRepository.getChatBySecondUser(user.get());

            if (chat.isEmpty()) {
                throw new AppException("empty chat",HttpStatus.BAD_REQUEST);
            } else {
                return chat;
            }
        }else{
            throw new AppException("user doesn't exist", HttpStatus.BAD_REQUEST);
        }

    }


    public HashSet<Chat> getChatByFirstUserNameOrSecondUserName(String username) throws AppException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            HashSet<Chat> chat = chatRepository.getChatByFirstUser(user.get());
            HashSet<Chat> chat1 = chatRepository.getChatBySecondUser(user.get());

            chat1.addAll(chat);

            if (chat.isEmpty() && chat1.isEmpty()) {
                throw new AppException("empty chat",HttpStatus.BAD_REQUEST);
            } else if (chat1.isEmpty()) {
                return chat;
            } else {
                return chat1;
            }
        }else{
            throw new AppException("user doesn't exist", HttpStatus.BAD_REQUEST);
        }

    }


    public HashSet<Chat> getChatByFirstUserNameAndSecondUserName(String firstUserName, String secondUserName) throws AppException {
        Optional<User> firstuser = userRepository.findByUsername(firstUserName);
        Optional<User> seconduser = userRepository.findByUsername(secondUserName);
        if(firstuser.isPresent() && seconduser.isPresent()){
            HashSet<Chat> chat = chatRepository.getChatByFirstUserAndSecondUser(firstuser.get(), seconduser.get());
            HashSet<Chat> chat1 = chatRepository.getChatBySecondUserAndFirstUser(firstuser.get(), seconduser.get());
            if (chat.isEmpty() && chat1.isEmpty()) {
                throw new AppException();
            } else if (chat.isEmpty()) {
                return chat1;
            } else {
                return chat;
            }
        }else{
            throw new AppException("user doesn't exist", HttpStatus.BAD_REQUEST);
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
