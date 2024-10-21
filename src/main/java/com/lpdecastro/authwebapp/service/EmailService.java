package com.lpdecastro.authwebapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("liandrejohn88@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    @Async
    public void sendRegistrationConfirmationEmail(String to, String firstName, String confirmationLink) throws MessagingException, IOException {
        String emailContent = prepareEmailContent("email-templates/registration-confirmation.html", firstName, confirmationLink);
        sendHtmlEmail(to, "Confirm Your Email - [YourAppName]", emailContent);
    }

    @Async
    public void sendResendEmailVerification(String to, String firstName, String confirmationLink) throws MessagingException, IOException {
        String emailContent = prepareEmailContent("email-templates/resend-email-verification.html", firstName, confirmationLink);
        sendHtmlEmail(to, "Resend Email Verification - [YourAppName]", emailContent);
    }

    @Async
    public void sendResetPasswordEmail(String to, String firstName, String resetLink) throws MessagingException, IOException {
        String emailContent = prepareEmailContent("email-templates/reset-password.html", firstName, resetLink);
        sendHtmlEmail(to, "Reset Your Password - [YourAppName]", emailContent);
    }

    @Async
    public void sendPasswordChangedEmail(String to, String firstName) throws MessagingException, IOException {
        String emailContent = prepareEmailContent("email-templates/password-changed.html", firstName, null);
        sendHtmlEmail(to, "Password Changed Successfully - [YourAppName]", emailContent);
    }

    @Async
    public void sendEmailChangedVerificationEmail(String to, String firstName, String emailVerificationLink) throws MessagingException, IOException {
        String emailContent = prepareEmailContent("email-templates/email-changed.html", firstName, emailVerificationLink);
        sendHtmlEmail(to, "Verify Your New Email - [YourAppName]", emailContent);
    }

    // Helper method to load the template from resources and replace placeholders
    private String prepareEmailContent(String templatePath, String firstName, String actionLink) throws IOException {
        String emailTemplate = loadEmailTemplate(templatePath);
        if (firstName != null) {
            emailTemplate = emailTemplate.replace("${firstName}", firstName);
        }
        if (actionLink != null) {
            emailTemplate = emailTemplate.replace("${confirmationLink}", actionLink);
            emailTemplate = emailTemplate.replace("${resetLink}", actionLink);
            emailTemplate = emailTemplate.replace("${emailVerificationLink}", actionLink);
        }
        return emailTemplate;
    }

    // Sends an HTML email
    private void sendHtmlEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);  // true means the message is HTML
        javaMailSender.send(message);
    }

    // Helper method to load the template from resources
    private String loadEmailTemplate(String templatePath) throws IOException {
        Path path = new ClassPathResource(templatePath).getFile().toPath();
        return new String(Files.readAllBytes(path));
    }
}
