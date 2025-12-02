package com.johan.weather_app_notification.rabbit.producer;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.UserIdCityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthProducer {

    private static final Logger logger = LoggerFactory.getLogger(AuthProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public AuthProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publicerar ett meddelande på kön för att begära användarens e-postadress.
     * Mottagare: Auth-tjänsten via RabbitMQ.
     */
    public void sendAuthRequest(UserIdCityDto dto) {
        logger.info("Publicerar auth-request till RabbitMQ för userId: {}", dto.userId());

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_WEATHER,
                RabbitConfig.ROUTING_KEY_AUTH_REQUEST,
                dto
        );

        logger.info("Meddelande publicerat till exchange: {}", RabbitConfig.EXCHANGE_WEATHER);
    }
}