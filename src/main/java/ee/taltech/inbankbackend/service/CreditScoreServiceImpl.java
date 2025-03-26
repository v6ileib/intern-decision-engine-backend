package ee.taltech.inbankbackend.service;

import org.springframework.stereotype.Service;

@Service
public class CreditScoreServiceImpl implements CreditScoreService {

    @Override
    public double calculate(int loanAmount, int loanPeriod, int creditModifier) {
        if (loanAmount <= 0 || loanPeriod <= 0) {
            throw new IllegalArgumentException("Loan amount and period must be greater than 0.");
        }
        return ((double) creditModifier / loanAmount) * loanPeriod / 10;
    }
}