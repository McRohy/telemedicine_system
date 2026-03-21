package sk.uniza.fri.telemedicine.services.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//podla: https://www.geeksforgeeks.org/springboot/spring-boot-sending-email-via-smtp/
// https://mailtrap.io/blog/spring-send-email/
@Slf4j
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    @Async
    public void sendMeasurementRecordAlert(String careProviderEmail, String patientFullName, Double value, String units) {
        String subject = "Upozornenie na meranie mimo normy";
        String body = "Pacient " + patientFullName + " zaznamenal meranie s hodnotou " + value + " " + units + ", čo je mimo normálneho rozsahu.\n" +
                "Prosím, skontrolujte meranie pacienta.";
        sendEmail(careProviderEmail, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailWithTokenPassword(String to, String passwordUrl) {
        String subject = "Nastavenie hesla pre váš účet";
        String body = "Zasielame link pre nastavenie hesla vášho účtu.\n" +
                "Kliknite na nasledujúci odkaz pre nastavenie hesla:\n" +
                passwordUrl;
        sendEmail(to, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailSuccessfulPasswordSetUp(String to) {
        String subject = "Úspešné nastavenie hesla";
        String body = "Vaše heslo bolo úspešne nastavené.\n" +
                "Môžte sa prihlásiť do systému a využívať telemedicínsky systém naplno.";
        sendEmail(to, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailCreatedPlan(String to) {
        String subject = "Plán vytvorený";
        String body = "Váš plán pre vzdialené monitorovanie bol vytvorený.\n" +
                "Prihláste sa do systému a skontrolujte svoj plán pre vzdialené monitorovanie.";
        sendEmail(to, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailUpdatedPlan(String to) {
        String subject = "Plán upravený";
        String body = "Váš plán pre vzdialené monitorovanie bol upravený.\n" +
                "Skontrolujte si svoj plán pre vzdialené monitorovanie.";
        sendEmail(to, subject, createEmailContent(body));
    }

    private String createEmailContent(String body) {
        return "Dobrý deň,\n\n" +
                body + "\n\n" +
                "S pozdravom,\n Tím MediRoh";
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(content);
            message.setSubject(subject);
            mailSender.send(message);
            log.info("Email was sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
