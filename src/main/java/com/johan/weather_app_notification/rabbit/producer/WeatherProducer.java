package com.johan.weather_app_notification.rabbit.producer;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.Email_City_DTO;
import com.johan.weather_app_notification.dto.UserId_City_DTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class WeatherProducer {

    private final RabbitTemplate rabbitTemplate;

    public WeatherProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendWeatherData(Email_City_DTO dto) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.WEATHER_EXCHANGE,
                RabbitConfig.WEATHER_WEATHER_REQUEST_ROUTING_KEY,
                dto  // ✅ KORREKT: Skickar hela DTO objektet
        );
    }
}