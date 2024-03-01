package com.esticharalegal.backendServer.service;


import com.esticharalegal.backendServer.exceptions.AppException;
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


    public List<Lawyer> getall() throws AppException {
        List< Lawyer> users = lawyerRepository.findAll();
        if (users.isEmpty()) {
            throw new  AppException();
        } else {
            return users;
        }
    }

    public  Lawyer addLawyer(Lawyer client) throws AppException {
        Optional<Lawyer> user1 = lawyerRepository.findByLicenseNumber(client.getLicenseNumber());

        if (user1.isPresent()) {
            throw new  AppException();
        } else {
            userRepository.save(client.getUser());
            return lawyerRepository.save(client);
        }
    }



}
