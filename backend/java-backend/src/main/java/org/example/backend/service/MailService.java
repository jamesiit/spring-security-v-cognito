package org.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender emailSender;
    private final String username;

    public MailService(JavaMailSender emailSender, @Value("${spring.mail.username}") String username) {
        this.emailSender = emailSender;
        this.username = username;
    }

    @Async
    public void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(username);
        message.setSubject("Hello World!");
        message.setText("Hello World!");
        emailSender.send(message);
    }

}
