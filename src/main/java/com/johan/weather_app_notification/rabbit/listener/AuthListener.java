package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuthListener {

    private final WeatherProducer weatherProducer;

    public AuthListener(WeatherProducer weatherProducer) {
        this.weatherProducer = weatherProducer;
    }

    @RabbitListener(queues = RabbitConfig.WEATHER_AUTH_RESPONSE_QUEUE)
    public void handleWeatherResponse(WeatherProducerDTO dto) {
        try {

            weatherProducer.sendWeatherData(dto);


        } catch (Exception e) {
            System.out.println("Error handling weather response" + e);
        }
    }
}
