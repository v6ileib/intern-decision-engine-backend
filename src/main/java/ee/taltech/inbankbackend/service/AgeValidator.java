package ee.taltech.inbankbackend.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import static ee.taltech.inbankbackend.config.DecisionEngineConstants.MAX_LOAN_PERIOD_YEARS;
import static ee.taltech.inbankbackend.config.DecisionEngineConstants.MIN_AGE;

public class AgeValidator {
    private static final Map<String, Integer> LIFE_EXPECTANCY = Map.of(
            "EE", 82,
            "LV", 75,
            "LT", 76
    );

    public boolean isEligible(LocalDate birthDate, String countryCode) {
        if (!LIFE_EXPECTANCY.containsKey(countryCode)) {
            return false;
        }

        int age = calculateAge(birthDate);
        int maxAge = LIFE_EXPECTANCY.get(countryCode) - MAX_LOAN_PERIOD_YEARS;

        return age >= MIN_AGE && age <= maxAge;
    }

    public int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String getRejectionReason(LocalDate birthDate, String countryCode) {
        if (!LIFE_EXPECTANCY.containsKey(countryCode)) {
            return "Unsupported country for age validation.";
        }

        int age = calculateAge(birthDate);
        int maxAge = LIFE_EXPECTANCY.get(countryCode) - MAX_LOAN_PERIOD_YEARS;

        if (age < MIN_AGE) {
            return "Applicant is underage.";
        } else if (age > maxAge) {
            return "Applicant exceeds maximum eligible age.";
        }

        return "Eligible";
    }
}