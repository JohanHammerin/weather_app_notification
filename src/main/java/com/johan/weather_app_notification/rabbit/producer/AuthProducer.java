package com.johan.weather_app_notification.rabbit.producer;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import com.johan.weather_app_notification.dto.reciever.AuthenticationRecieverDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthProducer {

    private final RabbitTemplate rabbitTemplate;



    @Autowired
    public AuthProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void getEmail(AuthenticationRecieverDTO authenticationRecieverDTO) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.WEATHER_EXCHANGE,
                RabbitConfig.WEATHER_AUTH_REQUEST_ROUTING_KEY,
                authenticationRecieverDTO  // ✅ KORREKT: Skickar hela DTO objektet
        );
    }
}
