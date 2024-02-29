package com.esticharalegal.backendServer.service;



import com.esticharalegal.backendServer.exceptions.ClientAlreadyExistException;
import com.esticharalegal.backendServer.exceptions.ClientNotFoundException;
import com.esticharalegal.backendServer.model.Client;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.ClientRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ClientService{

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserRepository userRepository;


    public List<Client> getall() throws ClientNotFoundException {
        List< Client> users = clientRepository.findAll();
        if (users.isEmpty()) {
            throw new  ClientNotFoundException();
        } else {
            return users;
        }
    }

    public  Client addClient(Client client) throws ClientAlreadyExistException {
        Optional<User> user1 = userRepository.findByUsername(client.getUser().getUsername());

        if (user1.isPresent()) {
            throw new  ClientAlreadyExistException();
        } else {
            userRepository.save(client.getUser());
            return clientRepository.save(client);
        }
    }




}
