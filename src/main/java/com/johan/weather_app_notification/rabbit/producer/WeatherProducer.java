package com.johan.weather_app_notification.rabbit.producer;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class WeatherProducer {

    private final RabbitTemplate rabbitTemplate;

    public WeatherProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendWeatherData(WeatherProducerDTO weatherData) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.WEATHER_EXCHANGE,
                RabbitConfig.WEATHER_WEATHER_REQUEST_ROUTING_KEY,
                weatherData  // ✅ KORREKT: Skickar hela DTO objektet
        );
    }
}