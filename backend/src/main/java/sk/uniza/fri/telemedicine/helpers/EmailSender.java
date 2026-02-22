package sk.uniza.fri.telemedicine.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String to, String subject, String content)
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


}
