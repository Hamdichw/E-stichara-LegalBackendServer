package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.*;
import com.esticharalegal.backendServer.model.Chat;
import com.esticharalegal.backendServer.model.Message;
import com.esticharalegal.backendServer.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/add")
    public ResponseEntity<String> createChat(@RequestBody Chat chat) throws AppException {

        return new ResponseEntity<String>(chatService.addChat(chat), HttpStatus.CREATED);
    }

    @PostMapping("/add/message1")
    public ResponseEntity<Message> addMessage2(@RequestBody Message message)  {
            return new ResponseEntity<Message>(chatService.addMessage2(message), HttpStatus.CREATED);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<HashSet<Chat>> getAllChatsClients(@PathVariable long userId) {
        try {
            return new ResponseEntity<HashSet<Chat>>(chatService.findallchatsClients(userId), HttpStatus.OK);
        } catch (AppException e) {
           return new ResponseEntity("List not found", HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/all/Lawyers/{userId}")
    public ResponseEntity<HashSet<Chat>> getAllChatsLawyers(@PathVariable long userId) {
        try {
            return new ResponseEntity<HashSet<Chat>>(chatService.findallchatsLawyers(userId), HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity("List not found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/last/message/from/chat/{chatId}")
    public ResponseEntity<?> getLastMessageInChat(@PathVariable int chatId) {
        try {
            Message lastMessage = this.chatService.getLastMessageInChat(chatId);
            if (lastMessage != null) {
                return ResponseEntity.ok(lastMessage);
            } else {
                return new ResponseEntity("No messages found in the chat", HttpStatus.NOT_FOUND);
            }
        } catch (AppException e) {
            return new ResponseEntity("Chat not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all/messages/from/chat/{chatId}")
    public ResponseEntity<?> getAllMessagesInChat(@PathVariable int chatId) {
        try {
            Chat chat = new Chat();
            chat.setChatId(chatId);
            List<Message> messageList = this.chatService.getAllMessagesInChat(chatId);
            return ResponseEntity.ok(messageList);
        } catch (AppException e) {
            return new ResponseEntity("Message List not found", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chat> getChatById(@PathVariable int id) {
        try {
            return new ResponseEntity<Chat>(chatService.getById(id), HttpStatus.OK);
        } catch (AppException e) {
           return new ResponseEntity("Chat Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/firstUserName/{username}")
    public ResponseEntity<?> getChatByFirstUserName(@PathVariable String username) {
        try {
            HashSet<Chat> byChat = this.chatService.getChatByFirstUserName(username);
            return new ResponseEntity<>(byChat, HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity("Chat Not Exits", HttpStatus.CONFLICT);
        }
    }



    @GetMapping("/secondUserName/{username}")
    public ResponseEntity<?> getChatBySecondUserName(@PathVariable String username) {

        try {
            HashSet<Chat> byChat = this.chatService.getChatBySecondUserName(username);
            return new ResponseEntity<>(byChat, HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity("Chat Not Exits", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getChatByFirstUserNameOrSecondUserName/{username}")
    public ResponseEntity<?> getChatByFirstUserNameOrSecondUserName(@PathVariable String username) {

        try {
            HashSet<Chat> byChat = this.chatService.getChatByFirstUserNameOrSecondUserName(username);
            return new ResponseEntity<>(byChat, HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity("Chat Not Exits", HttpStatus.CONFLICT);
        }
    }


    @GetMapping("/getChatByFirstUserNameAndSecondUserName")
    public ResponseEntity<?> getChatByFirstUserNameAndSecondUserName(@RequestParam("firstUserName") String firstUserName, @RequestParam("secondUserName") String secondUserName){

        try {
            HashSet<Chat> chatByBothEmail = this.chatService.getChatByFirstUserNameAndSecondUserName(firstUserName, secondUserName);
            return new ResponseEntity<>(chatByBothEmail, HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity("Chat Not Exits", HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/message/{chatId}")
    public ResponseEntity<Chat> addMessage(@RequestBody Message add , @PathVariable int chatId) throws AppException {
        return new ResponseEntity<Chat>(chatService.addMessage(add,chatId), HttpStatus.OK);
    }

}
