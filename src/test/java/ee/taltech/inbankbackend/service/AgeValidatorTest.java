package ee.taltech.inbankbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AgeValidatorTest {

    private AgeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AgeValidator();
    }

    @Test
    void testAgeExactly18IsEligible() {
        LocalDate birthDate = LocalDate.now().minusYears(18);
        assertTrue(validator.isEligible(birthDate, "EE"));
    }

    @Test
    void testUnderageIsNotEligible() {
        LocalDate birthDate = LocalDate.now().minusYears(17).plusDays(1);
        assertFalse(validator.isEligible(birthDate, "EE"));
        assertEquals("Applicant is underage.", validator.getRejectionReason(birthDate, "EE"));
    }

    @Test
    void testMaxEligibleAgeIsEligible() {
        LocalDate birthDate = LocalDate.now().minusYears(78);
        assertTrue(validator.isEligible(birthDate, "EE"));
    }

    @Test
    void testAgeOverMaxIsNotEligible() {
        LocalDate birthDate = LocalDate.now().minusYears(79);
        assertFalse(validator.isEligible(birthDate, "EE"));
        assertEquals("Applicant exceeds maximum eligible age.", validator.getRejectionReason(birthDate, "EE"));
    }

    @Test
    void testUnsupportedCountryIsNotEligible() {
        LocalDate birthDate = LocalDate.now().minusYears(30);
        assertFalse(validator.isEligible(birthDate, "FI"));
        assertEquals("Unsupported country for age validation.", validator.getRejectionReason(birthDate, "FI"));
    }
}