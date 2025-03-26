package ee.taltech.inbankbackend.service;

public interface CreditScoreService {
    double calculate(int loanAmount, int loanPeriod, int creditModifier);
}
