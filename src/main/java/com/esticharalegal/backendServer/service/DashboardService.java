package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.TransactionRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DashboardService {
    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    // transations dashboard
    public BigDecimal getCurrentMonthIncome(Long idLawyer) {
        return transactionRepository.getCurrentMonthIncome(idLawyer);
    }

    public BigDecimal getCurrentMonthOutcome(Long idLawyer) {
        return transactionRepository.getCurrentMonthOutcome(idLawyer);
    }

    public BigDecimal getCurrentMonthGain(Long idLawyer) {
        return transactionRepository.getCurrentMonthGain(idLawyer);
    }

    public List<Object[]> getMonthlyIncomeAndOutcomeFor12Months(Long idLawyer) {
        return transactionRepository.getMonthlyIncomeAndOutcomeFor12Months(idLawyer);
    }


    // users dashboard



    public AgeGroupCount getAgeGroupCount(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            // Handle case where user with given ID is not found
            return null;
        }

        AgeGroupCount ageGroupCount = new AgeGroupCount();

        for (User connection : user.getConnections()) {
            int age = calculateAge(connection.getBirthday());
            if (age < 20) {
                ageGroupCount.incrementUnder20();
            } else if (age < 40) {
                ageGroupCount.incrementUnder40();
            } else {
                ageGroupCount.incrementOver40();
            }
        }
        return ageGroupCount;
    }

    // Method to calculate the age based on the birthday
    private int calculateAge(Date birthday) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthday);
        int birthYear = cal.get(Calendar.YEAR);
        int birthMonth = cal.get(Calendar.MONTH);
        int birthDay = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(new Date());
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }
        return age;
    }

    public static class AgeGroupCount {
        private int under20;
        private int under40;
        private int over40;

        public int getUnder20() {
            return under20;
        }

        public void incrementUnder20() {
            under20++;
        }

        public int getUnder40() {
            return under40;
        }

        public void incrementUnder40() {
            under40++;
        }

        public int getOver40() {
            return over40;
        }

        public void incrementOver40() {
            over40++;
        }
    }
}