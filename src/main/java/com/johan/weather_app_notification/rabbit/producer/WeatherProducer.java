package com.johan.weather_app_notification.rabbit.producer;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.EmailCityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class WeatherProducer {

    private static final Logger logger = LoggerFactory.getLogger(WeatherProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public WeatherProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Skickar en förfrågan till vädertjänsten om att hämta väderdata för en specifik stad.
     * Skickar med e-postadressen så att vädertjänsten kan skicka svaret vidare.
     */
    public void sendWeatherRequest(EmailCityDto dto) {
        logger.info("Skickar väderförfrågan till RabbitMQ för stad: {} och email: {}",
                dto.city(), dto.email());

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_WEATHER,
                RabbitConfig.ROUTING_KEY_WEATHER_REQUEST,
                dto
        );

        logger.info("Väderförfrågan publicerad.");
    }
}