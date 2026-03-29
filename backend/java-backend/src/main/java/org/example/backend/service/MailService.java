package org.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender emailSender;
    private final String senderEmail;

    public MailService(JavaMailSender emailSender, @Value("${spring.mail.username}") String senderEmail) {
        this.emailSender = emailSender;
        this.senderEmail = senderEmail;
    }

    @Async
    public void sendSimpleMessage(String dtoUsername, String newCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(dtoUsername);
        message.setSubject("Your OTP!");
        message.setText("Your OTP is: " + newCode);
        emailSender.send(message);
    }
}
