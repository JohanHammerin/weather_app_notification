package com.johan.weather_app_notification.rabbit;

import com.johan.weather_app_notification.dto.SubscriptionEventDTO;
import com.johan.weather_app_notification.service.MailService;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionEventListener {

    private final MailService mailService;

    @Autowired
    public SubscriptionEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "weather-mail-queue")
    public void handleSubscriptionEvent(@Valid SubscriptionEventDTO event) {
        System.out.println("Processing event for user: " + event.userId() + ", city: " + event.city());

        try {
            String userEmail = "amanda.lyckenius@stud.sti.se";
            String emailText = "Hej! Här är väderuppdateringen för " + event.city();
            mailService.sendMail(userEmail, "Vädernotis", emailText);
            System.out.println("Successfully processed event for userId " + event.userId());
        } catch (Exception e) {
            System.err.println("Failed to process event: " + e.getMessage());
            // Vänta 30 sekunder innan retry
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            throw e;
        }
    }

}
