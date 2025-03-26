package ee.taltech.inbankbackend.service;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
public class DecisionEngine {

    private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();
    private final PersonalCodeHandler personalCodeHandler = new PersonalCodeHandler();
    private final AgeValidator ageValidator = new AgeValidator();

    public Decision calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
            NoValidLoanException {

        verifyInputs(personalCode, loanAmount, loanPeriod);
        LocalDate birthDate;
        try {
            birthDate = personalCodeHandler.getBirthDate(personalCode);
        } catch (DateTimeException e) {
            throw new InvalidPersonalCodeException("Invalid personal ID code!");
        }
        String countryCode = personalCodeHandler.getCountryCode(personalCode);

        if (!ageValidator.isEligible(birthDate, countryCode)) {
            throw new NoValidLoanException(ageValidator.getRejectionReason(birthDate, countryCode));
        }

        int creditModifier = getCreditModifier(personalCode);
        if (creditModifier == 0) {
            throw new NoValidLoanException("Customer is in debt.");
        }

        for (int period = loanPeriod; period <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD; period++) {
            for (int amount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT;
                 amount >= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT;
                 amount -= 100) {

                if (isLoanApprovable(amount, period, creditModifier)) {
                    return new Decision(amount, period, null);
                }
            }
        }

        throw new NoValidLoanException("Valid loan not found!");
    }

    private boolean isLoanApprovable(int amount, int period, int creditModifier) {
        return calculateCreditScore(amount, period, creditModifier) >= 0.1;
    }

    private double calculateCreditScore(int amount, int period, int creditModifier) {
        return ((double) creditModifier / amount) * period / 10;
    }

    private int getCreditModifier(String personalCode) {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));

        if (segment < 2500) {
            return 0;
        } else if (segment < 5000) {
            return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        }

        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }

    private void verifyInputs(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException {

        if (personalCode.length() != 11 || !personalCode.matches("\\d{11}")) {
            throw new InvalidPersonalCodeException("Personal ID code must be 11 digits.");
        }
        if (!(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT <= loanAmount)
                || !(loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT)) {
            throw new InvalidLoanAmountException("Invalid loan amount!");
        }
        if (!(DecisionEngineConstants.MINIMUM_LOAN_PERIOD <= loanPeriod)
                || !(loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD)) {
            throw new InvalidLoanPeriodException("Invalid loan period!");
        }
    }
}