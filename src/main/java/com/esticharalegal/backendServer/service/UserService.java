package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.exceptions.UserAlreadyExistException;
import com.esticharalegal.backendServer.exceptions.UserNotFoundException;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getall() throws UserNotFoundException {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException();
        } else {
            return users;
        }
    }

    public User addUser(User user) throws UserAlreadyExistException {
        Optional<User> user1 = userRepository.findByUsername(user.getUsername());

        if (user1.isPresent()) {
            throw new UserAlreadyExistException();
        } else {
            return userRepository.save(user);
        }
    }


    public User getUserByUserName(String username) throws UserNotFoundException {
        Optional<User> user1 = userRepository.findByUsername(username);

        if (user1.isPresent()) {
            return user1.get();
        } else {
            throw new UserNotFoundException();
        }
    }
}