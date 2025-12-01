package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.GlobalVariables.Globals;
import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.reciever.AuthReceiverDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuthListener {

    @RabbitListener(queues = RabbitConfig.WEATHER_AUTH_RESPONSE_QUEUE)
    public void handleWeatherResponse(AuthReceiverDTO dto) {
        Globals.GLOBAL_EMAIL = dto.email();
    }
}
