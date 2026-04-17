package sk.uniza.fri.telemedicine.service.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.config.TextProvider;

/**
 * Service for sending email notifications.
 * All methods are asynchronous to avoid blocking HTTP responses.
 * patterns: https://mailtrap.io/blog/spring-send-email/
 * https://www.geeksforgeeks.org/springboot/spring-boot-sending-email-via-smtp/
 */
@Slf4j
@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    private final JavaMailSender mailSender;
    private final TextProvider textProvider;

    public EmailService(JavaMailSender javaMailSender, TextProvider textProvider) {
        this.mailSender = javaMailSender;
        this.textProvider = textProvider;
    }

    @Async
    public void sendMeasurementRecordAlert(String careProviderEmail, String patientFullName, Double value, String units) {
        String subject = textProvider.get("email.measurement.alert.subject");
        String body = textProvider.get("email.measurement.alert.body", patientFullName, value, units);
        sendEmail(careProviderEmail, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailWithTokenPassword(String to, String passwordUrl) {
        String subject = textProvider.get("email.password.setup.subject");
        String body = textProvider.get("email.password.setup.body", passwordUrl);
        sendEmail(to, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailSuccessfulPasswordSetUp(String to) {
        String subject = textProvider.get("email.password.success.subject");
        String body = textProvider.get("email.password.success.body");
        sendEmail(to, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailCreatedPlan(String to) {
        String subject = textProvider.get("email.plan.created.subject");
        String body = textProvider.get("email.plan.created.body");
        sendEmail(to, subject, createEmailContent(body));
    }

    @Async
    public void sendEmailUpdatedPlan(String to) {
        String subject = textProvider.get("email.plan.updated.subject");
        String body = textProvider.get("email.plan.updated.body");
        sendEmail(to, subject, createEmailContent(body));
    }

    private String createEmailContent(String body) {
        return textProvider.get("email.common.greeting") + "\n\n" +
                body + "\n\n" +
                textProvider.get("email.common.signature");
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
