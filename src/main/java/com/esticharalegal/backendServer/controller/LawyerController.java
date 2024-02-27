package com.esticharalegal.backendServer.controller;


import com.esticharalegal.backendServer.exceptions.LawyerAlreadyExistException;
import com.esticharalegal.backendServer.model.Lawyer;
import com.esticharalegal.backendServer.service.LawyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/Lawyer")
public class LawyerController {
    @Autowired
    private LawyerService lawyerService;

    @PostMapping("/add")
    public ResponseEntity<Lawyer> addUser(@RequestBody Lawyer lawyer){
        try{
            return new ResponseEntity<Lawyer>(lawyerService.addLawyer(lawyer), HttpStatus.OK);

        }catch (LawyerAlreadyExistException e){
            return new ResponseEntity("User already exists", HttpStatus.CONFLICT);
        }
    }
}
