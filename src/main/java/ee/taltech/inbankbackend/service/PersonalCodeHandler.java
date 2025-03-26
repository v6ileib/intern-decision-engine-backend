package ee.taltech.inbankbackend.service;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeParser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PersonalCodeHandler {

    private final EstonianPersonalCodeParser parser = new EstonianPersonalCodeParser();

    public LocalDate getBirthDate(String personalCode) {
        try {
            return parser.getDateOfBirth(personalCode);
        } catch (PersonalCodeException e) {
            throw new IllegalArgumentException("Invalid personal code format: " + e.getMessage());
        }
    }

    public String getCountryCode(String personalCode) {
        return "EE";
    }
}
