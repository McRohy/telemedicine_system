package sk.uniza.fri.telemedicine.enums.others;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum EmailText {
    SUBJECT_ALERT_RECORD("Telemedicina - Upozornenie na meranie mimo normy"),
    SUBJECT_ACCOUNT_PASSWORD("Telemedicina - Dočasné heslo"),
    CONTENT_ALERT_RECORD("Pacient: %s s hodnotou %.2f %s je mimo povolený rozsah"),
    CONTENT_ACCOUNT_PASSWORD("Vaše dočasné heslo je: %s");

    private final String text;
}
