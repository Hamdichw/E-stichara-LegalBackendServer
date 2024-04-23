package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.model.Transaction;
import com.esticharalegal.backendServer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Transaction> addTransactionByLawyerId(@RequestBody Transaction transaction, @PathVariable Long lawyerId) {
        Transaction newTransaction = transactionService.addTransactionByLawyerId(transaction, lawyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @DeleteMapping("/lawyer/{lawyerId}/{transactionId}")
    public ResponseEntity<Void> deleteTransactionByLawyerId(@PathVariable Long transactionId, @PathVariable Long lawyerId) {
        transactionService.deleteTransactionByLawyerId(transactionId, lawyerId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/lawyer/{lawyerId}/{transactionId}")
    public ResponseEntity<Transaction> updateTransactionByLawyerId(
            @PathVariable Long transactionId,
            @RequestBody Transaction updatedTransaction,
            @PathVariable Long lawyerId) {
        Transaction transaction = transactionService.updateTransactionByLawyerId(transactionId, updatedTransaction, lawyerId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.notFound().build();
    }
}
