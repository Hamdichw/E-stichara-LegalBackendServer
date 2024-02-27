package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.exceptions.UserAlreadyExistException;
import com.esticharalegal.backendServer.exceptions.UserNotFoundException;
import com.esticharalegal.backendServer.model.User;

import java.util.List;

public interface UserService {
    List<User> getall() throws UserNotFoundException;

    User addUser(User user) throws UserAlreadyExistException;

    User getUserByUserName(String username)  throws UserNotFoundException;
}
