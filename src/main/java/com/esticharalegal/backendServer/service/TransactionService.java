package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.model.Transaction;
import com.esticharalegal.backendServer.repository.TransactionRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;


    public List<Transaction> getAllByLawyerId(Long lawyerId) {
        return transactionRepository.findAllByLawyer_UserID(lawyerId);
    }

    public Transaction addTransactionByLawyerId(Transaction transaction, Long lawyerId) {
        transaction.setLawyer(userRepository.findById(lawyerId).get());
        return transactionRepository.save(transaction);
    }

    public void deleteTransactionByLawyerId(Long transactionId, Long lawyerId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isPresent() && transaction.get().getLawyer().getUserID() == lawyerId) {
            transactionRepository.deleteById(transactionId);
        }
    }

    public Transaction updateTransactionByLawyerId(Long transactionId, Transaction updatedTransaction, Long lawyerId) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            if (transaction.getLawyer().getUserID() == lawyerId) {
                if (updatedTransaction.getType() != null) {
                    transaction.setType(updatedTransaction.getType());
                }
                if (updatedTransaction.getDate() != null) {
                    transaction.setDate(updatedTransaction.getDate());
                }
                return transactionRepository.save(transaction);
            }
        }
        return null;
    }
}
