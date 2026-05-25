package com.dhij.app.com.dhij.app.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendWelcomeEmail(String to, String username) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("Welcome to DHij App!");
            msg.setFrom("imrankhan8669393@gmail.com"); // should match your authenticated Gmail or verified alias
            msg.setText(
                "Hi " + username + ",\n\n" +
                "Welcome to DHij App! 🎉\n\n" +
                "Your account has been created successfully.\n\n" +
                "You can now log in and book sessions.\n\n" +
                "Login: http://localhost:5173/login\n\n" +
                "Thanks,\nDHij Team"
            );
            mailSender.send(msg);
            System.out.println("✅ Welcome email sent to: " + to);
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}