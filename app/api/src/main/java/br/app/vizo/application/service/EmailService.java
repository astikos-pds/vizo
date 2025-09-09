package br.app.vizo.application.service;

import br.app.vizo.application.exception.base.InternalServerErrorException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.time.Instant;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String MAIL_USERNAME;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendVerificationCode(String to, String code, Instant expiresAt) {
        String subject = "Your verification code in Vizo is %s".formatted(code);

        long expirationInMinutes = Duration.between(Instant.now(), expiresAt).toMinutes();

        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("expiration", expirationInMinutes);

        String html = templateEngine.process("email/verification", context);

        try {
            this.sendHtmlEmail(to, subject, html);
        } catch(MessagingException e) {
            System.out.printf("Error sending e-mail to %s: %s\n", to, e.getMessage());
            throw new InternalServerErrorException("Error sending e-mail.");
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(MAIL_USERNAME);

        mailSender.send(message);
    }
}
