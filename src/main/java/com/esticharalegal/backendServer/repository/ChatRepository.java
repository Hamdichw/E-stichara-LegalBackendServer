package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Chat;
import com.esticharalegal.backendServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    HashSet<Chat> getChatByFirstUser(User firstUser);

    HashSet<Chat> getChatBySecondUser(User secondUser);

    HashSet<Chat> getChatByFirstUserAndSecondUser(User firstUser, User secondUser);

    HashSet<Chat> getChatBySecondUserAndFirstUser(User firstUser, User secondUser);
}
