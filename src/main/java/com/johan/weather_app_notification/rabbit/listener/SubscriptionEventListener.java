package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.GlobalVariables.Globals;
import com.johan.weather_app_notification.dto.producer.WeatherAuthProducerDTO;
import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import com.johan.weather_app_notification.dto.reciever.SubscriptionRecieverDTO;
import com.johan.weather_app_notification.rabbit.producer.AuthProducer;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEventListener.class);

    private final WeatherProducer weatherProducer;
    private final AuthProducer authProducer;

    public SubscriptionEventListener(WeatherProducer weatherProducer, AuthProducer authProducer) {
        this.weatherProducer = weatherProducer;
        this.authProducer = authProducer;
    }


        @RabbitListener(queues = "weather.subscription.due")
        public void handleSubscriptionEvent(SubscriptionRecieverDTO dto) throws InterruptedException {
            logger.info("🎯 Subscription event listener triggered");
            logger.info("📋 Processing - userId: {}, city: {}", dto.userId(), dto.city());
            Globals.setGlobalCity(dto.city());
            Globals.setGlobalUserid(dto.userId());

            // Skicka weather request med userId
            try {
                logger.info("🌤️ Sending weather data request for city: {}, userId: {}", dto.city(), dto.userId());
                WeatherProducerDTO weatherRequest = new WeatherProducerDTO(Globals.getGlobalCity()); // ✅ Skicka userId
                weatherProducer.sendWeatherData(weatherRequest);
                logger.info("✅ Weather data request sent successfully");
            } catch (Exception e) {
                logger.error("❌ Error sending weather data request for city: {}", dto.city(), e);
            }

            Thread.sleep(5000);
            // Skicka auth request
            try {
                logger.info("🔐 Sending auth request for userId: {}", dto.userId());
                WeatherAuthProducerDTO authRequest = new WeatherAuthProducerDTO(Globals.GLOBAL_USERID);
                authProducer.getEmail(authRequest);
                logger.info("✅ Auth request sent successfully");
            } catch (Exception e) {
                logger.error("❌ Error sending auth request for userId: {}", dto.userId(), e);
            }

            logger.info("🎉 Subscription event processing completed");
        }
    }