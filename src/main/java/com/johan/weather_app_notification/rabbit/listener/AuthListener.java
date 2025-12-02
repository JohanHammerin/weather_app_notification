package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.Email_City_DTO;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthListener {

    private final WeatherProducer weatherProducer;

    @Autowired
    public AuthListener(WeatherProducer weatherProducer) {
        this.weatherProducer = weatherProducer;
    }

    @RabbitListener(queues = RabbitConfig.WEATHER_AUTH_RESPONSE_QUEUE)
    public void handleAuthResponse(Email_City_DTO dto) {
        weatherProducer.sendWeatherData(dto);
    }
}
