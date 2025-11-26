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
        logger.info("🎯 Subscription event listener triggered");
        logger.debug("Received subscription DTO - userId: {}, city: {}", dto.userId(), dto.city());

        // Sätt globala variabler
        Globals.setGlobalUserId(dto.userId());
        Globals.setGlobalCity(dto.city());

        logger.debug("Globals set - userId: {}, city: {}", Globals.getGlobalUserId(), Globals.getGlobalCity());

        // Trigger weather data request
        try {
            logger.info("🌤️ Sending weather data request for city: {}", dto.city());
            weatherProducer.sendWeatherData(new WeatherProducerDTO(dto.city()));
            logger.info("✅ Weather data request sent successfully");
        } catch (Exception e) {
            logger.error("❌ Error sending weather data request for city: {}", dto.city(), e);
        }

        // Trigger auth request för att hämta email
        try {
            logger.info("🔐 Sending auth request for userId: {}", dto.userId());
            WeatherAuthProducerDTO weatherAuthProducerDTO = new WeatherAuthProducerDTO(dto.userId());
            authProducer.getEmail(weatherAuthProducerDTO);
            logger.info("✅ Auth request sent successfully");
        } catch (Exception e) {
            logger.error("❌ Error sending auth request for userId: {}", dto.userId(), e);
        }

        logger.info("🎉 Subscription event processing completed for userId: {}, city: {}", dto.userId(), dto.city());
    }
}