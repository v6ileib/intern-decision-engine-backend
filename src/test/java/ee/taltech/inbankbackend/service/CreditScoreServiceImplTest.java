package ee.taltech.inbankbackend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditScoreServiceImplTest {

    private final CreditScoreServiceImpl creditScoreService = new CreditScoreServiceImpl();

    @Test
    void calculate_validInputs_returnsCorrectScore() {
        int creditModifier = 1000;
        int loanAmount = 5000;
        int loanPeriod = 36;
        double result = creditScoreService.calculate(loanAmount, loanPeriod, creditModifier);
        assertEquals(0.72, result, 0.0001);
    }

    @Test
    void calculateTooLowCreditScore() {
        int creditModifier = 100;
        int loanAmount = 10000;
        int loanPeriod = 12;
        double score = creditScoreService.calculate(loanAmount, loanPeriod, creditModifier);
        assertTrue(score < 0.1);
    }

    @Test
    void calculateTooHighCreditScore() {
        int creditModifier = 1000;
        int loanAmount = 2000;
        int loanPeriod = 48;
        double score = creditScoreService.calculate(loanAmount, loanPeriod, creditModifier);
        assertTrue(score > 0.1);
    }

    @Test
    void calculateZeroLoanAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                creditScoreService.calculate(0, 24, 300));
    }

    @Test
    void calculateZeroLoanPeriodThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                creditScoreService.calculate(3000, 0, 300));
    }

    @Test
    void calculateNegativeValuesThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                creditScoreService.calculate(-5000, 36, 300));
        assertThrows(IllegalArgumentException.class, () ->
                creditScoreService.calculate(5000, -36, 300));
    }
}