package ee.taltech.inbankbackend.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Decision {
    private final Integer loanAmount;
    private final Integer loanPeriod;
    private final String errorMessage;
}
