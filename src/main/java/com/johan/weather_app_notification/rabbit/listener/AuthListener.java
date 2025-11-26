package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.Globals;
import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.reciever.AuthenticationRecieverDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuthListener {


    @RabbitListener(queues = RabbitConfig.WEATHER_AUTH_RESPONSE_QUEUE)
    public void handleWeatherResponse(AuthenticationRecieverDTO dto) {
        try {

            Globals.setGlobalEmail(dto.email());

        } catch (Exception e) {
            System.out.println("Error handling weather response" + e);
        }
    }
}
