package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Transaction;
import com.esticharalegal.backendServer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")

public class TransactionController {
    private final TransactionService transactionService ;
    @GetMapping("/lawyer/{lawyerId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByLawyerId(@PathVariable Long lawyerId) {
        List<Transaction> transactions = transactionService.getAllByLawyerId(lawyerId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/lawyer/{lawyerId}")
    public ResponseEntity<Transaction> addTransactionByLawyerId(
            @RequestParam("type") String type,
            @RequestParam("amount") String amount,
            @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") String date
            , @PathVariable Long lawyerId) {
        BigDecimal transactionAmount = new BigDecimal(amount);
        Date inputdate;
        try {
            inputdate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            // Handle parsing exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Transaction addTransaction = new Transaction();
        addTransaction.setAmount(transactionAmount); // this setAmount set a Bigdicimal
        addTransaction.setType(type);
        addTransaction.setDate(inputdate); // the  setDate had date
        Transaction newTransaction = transactionService.addTransactionByLawyerId(addTransaction, lawyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @DeleteMapping("/lawyer/{lawyerId}/{transactionId}")
    public void deleteTransactionByLawyerId(@PathVariable Long transactionId, @PathVariable Long lawyerId) throws AppException {
        transactionService.deleteTransactionByLawyerId(transactionId, lawyerId);
    }


    @PutMapping("/lawyer/{lawyerId}/{transactionId}")
    public ResponseEntity<Transaction> updateTransactionByLawyerId(
            @PathVariable Long transactionId,
            @RequestParam("type") String type,
            @RequestParam("amount") String amount,
            @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") String date ,
            @PathVariable Long lawyerId) {

        BigDecimal transactionAmount = new BigDecimal(amount);
        Date inputdate;
        try {
            inputdate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            // Handle parsing exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Transaction addTransaction = new Transaction();
        addTransaction.setAmount(transactionAmount); // this setAmount set a Bigdicimal
        addTransaction.setType(type);
        addTransaction.setDate(inputdate); // the  setDate had date
        Transaction transaction = transactionService.updateTransactionByLawyerId(transactionId, addTransaction, lawyerId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }
}
