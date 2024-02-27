package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.ClientAlreadyExistException;
import com.esticharalegal.backendServer.exceptions.UserAlreadyExistException;
import com.esticharalegal.backendServer.model.Client;
import com.esticharalegal.backendServer.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
public class ClientController {

       @Autowired
        private ClientService clientService;

    @PostMapping("/add")
    public ResponseEntity<Client> addUser(@RequestBody Client client){
        try{
            return new ResponseEntity<Client>(clientService.addClient(client), HttpStatus.OK);

        }catch (ClientAlreadyExistException e){
            return new ResponseEntity("User already exists", HttpStatus.CONFLICT);
        }
    }

    @GetMapping()
    public String sayHello(){
        return  "hello";
    }

}
