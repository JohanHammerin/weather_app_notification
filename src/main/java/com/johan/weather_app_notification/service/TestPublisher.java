package com.johan.weather_app_notification.service;

import com.johan.weather_app_notification.dto.SubscriptionEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TestPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSample() {
        SubscriptionEventDTO sample = new SubscriptionEventDTO(123L, "Stockholm");
        rabbitTemplate.convertAndSend("weather-mail-queue", sample);
        System.out.println("Sent sample payload!");
    }
}
