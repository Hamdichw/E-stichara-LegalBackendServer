package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/current-month-income/{idLawyer}")
    public ResponseEntity<BigDecimal> getCurrentMonthIncome(@PathVariable Long idLawyer) {
        try {
            BigDecimal currentMonthIncome = dashboardService.getCurrentMonthIncome(idLawyer);
            return new ResponseEntity<>(currentMonthIncome, HttpStatus.OK);
        } catch (Exception e) {
              
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current-month-outcome/{idLawyer}")
    public ResponseEntity<BigDecimal> getCurrentMonthOutcome(@PathVariable Long idLawyer) {
        try {
            BigDecimal currentMonthOutcome = dashboardService.getCurrentMonthOutcome(idLawyer);
            return new ResponseEntity<>(currentMonthOutcome, HttpStatus.OK);
        } catch (Exception e) {
              
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current-month-gain/{idLawyer}")
    public ResponseEntity<BigDecimal> getCurrentMonthGain(@PathVariable Long idLawyer) {
        try {
            BigDecimal currentMonthGain = dashboardService.getCurrentMonthGain(idLawyer);
            return new ResponseEntity<>(currentMonthGain, HttpStatus.OK);
        } catch (Exception e) {
              
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/monthly-income-outcome-12-months/{idLawyer}")
    public ResponseEntity<List<Object[]>> getMonthlyIncomeAndOutcomeFor12Months(@PathVariable Long idLawyer) {
        try {
            List<Object[]> monthlyIncomeAndOutcome = dashboardService.getMonthlyIncomeAndOutcomeFor12Months(idLawyer);
            return new ResponseEntity<>(monthlyIncomeAndOutcome, HttpStatus.OK);
        } catch (Exception e) {
              
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/demographics{idLawyer}")
    public ResponseEntity<?> getAgeGroupCount(@PathVariable Long idLawyer) {
        try {
            DashboardService.AgeGroupCount ageGroupCount = dashboardService.getAgeGroupCount(idLawyer);
            return new ResponseEntity<>(ageGroupCount, HttpStatus.OK);
        } catch (Exception e) {
           
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
