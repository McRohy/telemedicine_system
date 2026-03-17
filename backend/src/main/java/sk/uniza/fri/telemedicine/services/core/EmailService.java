package sk.uniza.fri.telemedicine.services.core;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.exception.EmailSendException;

//podla: https://www.geeksforgeeks.org/springboot/spring-boot-sending-email-via-smtp/
// https://mailtrap.io/blog/spring-send-email/
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    private void sendMail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setText(content);
            message.setSubject(subject);
            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendException("Pri posielani emailu nastala chyba " + to);
        }
    }

    public void sendMeasurementRecordAlert(String careProviderEmail, String patientFullName, Double value, String units) {
        String subject = "Upozornenie na meranie mimo normy";
        String content = "Dobrý deň,\n" +
                "Pacient" + patientFullName + " zaznamenal meranie s hodnotou" + value + " " + units + ", čo je mimo normálneho rozsahu. \n" +
                "Prosím, skontrolujte meranie pacienta.\n\n" +
                "S pozdravom,\n Tím MediRoh";

        sendMail(careProviderEmail, subject, content);
    }

    public void sendEmailWithTokenPassword(String to, String passwordUrl) {
        String subject = "Nastavenie hesla pre váš účet";
        String content = "Dobrý deň,\n" +
                "Zasielame link pre nastavenie hesla vášho účtu.\n" +
                "Kliknite na následujíci odkaz pre nastavenie hesla:\n" +
                passwordUrl + "\n\n" +
                "S pozdravom,\n Tím MediRoh";
        sendMail(to, subject, content);
    }

    public void sendEmailSuccessfulPasswordSetUp(String to) {
        String subject = "Úspešné nastavenie hesla";
        String content = "Dobrý deň,\n" +
                "Vaše heslo bolo úspešne nastavené. \n " +
                "Môzte sa prihlásiť do systému a využívať telemedicínsky systém naplno.\n\n" +
                "S pozdravom,\n Tím MediRoh";
        sendMail(to, subject, content);
    }

    public void sendEmailCreatedPlan(String to) {
        String subject = "Plán vytvorený";
        String content = "Dobrý deň,\n" +
                "Váš plán pre vzdialené monitorovanie bol  vytvorený." +
                " \n Prihláste sa do systému a skontrolujte svoj plán pre vzdialené monitorovanie.\n\n" +
                "S pozdravom,\n Tím MediRoh";
        sendMail(to, subject, content);
    }

    public void sendEmailUpdatedPlan(String to) {
        String subject = "Plán upravený";
        String content = "Dobrý deň,\n" +
                "Váš plán pre vzdialené monitorovanie bol upravený." +
                "\n Skontrolujte si svoj plán pre vzdialené monitorovanie.\n\n" +
                "S pozdravom,\n Tím MediRoh";
        sendMail(to, subject, content);
    }
}
