package ee.taltech.inbankbackend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PersonalCodeParser {
    public LocalDate getBirthDate(String personalCode) {
        if (personalCode == null || personalCode.length() < 7) {
            throw new IllegalArgumentException("Invalid personal code format");
        }

        int centuryCode = Integer.parseInt(personalCode.substring(0, 1));
        String year = personalCode.substring(1, 3);
        String month = personalCode.substring(3, 5);
        String day = personalCode.substring(5, 7);

        String fullYear = switch (centuryCode) {
            case 1, 2 -> "18" + year;
            case 3, 4 -> "19" + year;
            case 5, 6 -> "20" + year;
            case 7, 8 -> "21" + year;
            default -> throw new IllegalArgumentException("Unsupported century code");
        };

        return LocalDate.parse(fullYear + "-" + month + "-" + day, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String getCountryCode(String personalCode) {
        return "EE";
    }
}