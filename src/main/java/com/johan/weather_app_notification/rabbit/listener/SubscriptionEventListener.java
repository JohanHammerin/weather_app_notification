package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.Globals;
import com.johan.weather_app_notification.dto.producer.WeatherAuthProducerDTO;
import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import com.johan.weather_app_notification.dto.reciever.SubscriptionRecieverDTO;
import com.johan.weather_app_notification.rabbit.producer.AuthProducer;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import com.johan.weather_app_notification.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEventListener.class);

    private final WeatherProducer weatherProducer;
    private final AuthProducer authProducer;
    private final MailService mailService;

    @Autowired
    public SubscriptionEventListener(WeatherProducer weatherProducer, AuthProducer authProducer, MailService mailService) {
        this.weatherProducer = weatherProducer;
        this.authProducer = authProducer;
        this.mailService = mailService;
    }

    @RabbitListener(queues = "weather.subscription.due")
    public void handleSubscriptionEvent(SubscriptionRecieverDTO dto) {
        System.out.println(dto.city() + dto.userId());

        Globals.setGlobalUserId(dto.userId());
        Globals.setGlobalCity(dto.city());

        // Trigger weather data request
        weatherProducer.sendWeatherData(new WeatherProducerDTO(dto.city()));
        authProducer.getEmail(new WeatherAuthProducerDTO(dto.userId()));

        logger.info("✅ Weather data requested for city: {}", dto.city());
    }




}

