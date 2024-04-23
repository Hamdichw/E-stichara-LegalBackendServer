package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT COALESCE(SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END), 0) - " +
            "COALESCE(SUM(CASE WHEN t.type = 'outcome' THEN t.amount ELSE 0 END), 0) " +
            "FROM Transaction t WHERE MONTH(t.date) = MONTH(CURRENT_DATE()) AND t.lawyer.userID = ?1")
    BigDecimal getCurrentMonthGain(Long idLawyer);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'income' " +
            "AND MONTH(t.date) = MONTH(CURRENT_DATE()) AND t.lawyer.userID = ?1")
    BigDecimal getCurrentMonthIncome(Long idLawyer);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'outcome' " +
            "AND MONTH(t.date) = MONTH(CURRENT_DATE()) AND t.lawyer.userID = ?1")
    BigDecimal getCurrentMonthOutcome(Long idLawyer);

    @Query("SELECT MONTH(t.date) AS month, " +
            "SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END) AS income, " +
            "SUM(CASE WHEN t.type = 'outcome' THEN t.amount ELSE 0 END) AS outcome " +
            "FROM Transaction t " +
            "WHERE YEAR(t.date) = YEAR(CURRENT_DATE) AND t.lawyer.userID = ?1 " +
            "GROUP BY MONTH(t.date) " +
            "ORDER BY MONTH(t.date)")
    List<Object[]> getMonthlyIncomeAndOutcomeFor12Months(Long idLawyer);


    List<Transaction> findAllByLawyer_UserID(Long userId);
}
