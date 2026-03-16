package sk.uniza.fri.telemedicine.helpers;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import sk.uniza.fri.telemedicine.enums.others.EmailText;

@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendMail(String to, String subject, String content)
    {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setText(content);
            message.setSubject(subject);
            javaMailSender.send(message);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendMeasurementRecordAlert(String careProviderEmail, String patientFullName, Double value, String units) {
        sendMail(careProviderEmail, EmailText.SUBJECT_ALERT_RECORD.getText(),
                EmailText.CONTENT_ALERT_RECORD.getText().formatted(patientFullName, value, units));
    }

    public void sendEmailWithPassword(String to, String temporaryPassword) {
        sendMail(to, EmailText.SUBJECT_ACCOUNT_PASSWORD.getText(),
                EmailText.CONTENT_ACCOUNT_PASSWORD.getText().formatted(temporaryPassword));
    }
}
