package com.johan.weather_app_notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.google.common.util.concurrent.RateLimiter;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final RateLimiter rateLimiter;
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        // Skapa RateLimiter direkt i konstruktorn istället för att autowire
        this.rateLimiter = RateLimiter.create(0.1); // 0.1 = 1 mail per 10 sekunder
    }

    public void sendMail(String to, String subject, String text) {
        rateLimiter.acquire(); // Väntar om nödvändigt
        try {
            logger.info("Attempting to send email to: {}", to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("moistusinc@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);


            mailSender.send(message);
            logger.info("✅ Email sent successfully to: {}", to);

        } catch (MailException e) {
            logger.error("❌ Failed to send email to {}: {}", to, e.getMessage());
            e.printStackTrace(); // Detta visar hela stacktracen
        }
    }
}