package ee.taltech.inbankbackend.service;

import com.github.vladislavgoltjajev.personalcode.exception.PersonalCodeException;
import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeParser;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PersonalCodeHandler {

    private final EstonianPersonalCodeParser parser = new EstonianPersonalCodeParser();

    public LocalDate getBirthDate(String personalCode) throws InvalidPersonalCodeException {
        try {
            return parser.getDateOfBirth(personalCode);
        } catch (PersonalCodeException e) {
            throw new InvalidPersonalCodeException("Invalid personal code format: " + e.getMessage(), e);

        }
    }

    public String getCountryCode(String personalCode) {
        return "EE";
    }
}
