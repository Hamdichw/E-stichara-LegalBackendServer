package com.esticharalegal.backendServer.service;


import com.esticharalegal.backendServer.exceptions.LawyerAlreadyExistException;
import com.esticharalegal.backendServer.exceptions.LawyerNotFoundException;
import com.esticharalegal.backendServer.model.Lawyer;
import com.esticharalegal.backendServer.repository.LawyerRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LawyerService {

    @Autowired
    private LawyerRepository lawyerRepository;
    @Autowired
    private UserRepository userRepository;


    public List<Lawyer> getall() throws LawyerNotFoundException {
        List< Lawyer> users = lawyerRepository.findAll();
        if (users.isEmpty()) {
            throw new  LawyerNotFoundException();
        } else {
            return users;
        }
    }

    public  Lawyer addLawyer(Lawyer client) throws LawyerAlreadyExistException {
        Optional<Lawyer> user1 = lawyerRepository.findByLicenseNumber(client.getLicenseNumber());

        if (user1.isPresent()) {
            throw new  LawyerAlreadyExistException();
        } else {
            userRepository.save(client.getUser());
            return lawyerRepository.save(client);
        }
    }



}
