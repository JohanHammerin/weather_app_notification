package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.Globals;
import com.johan.weather_app_notification.dto.producer.WeatherAuthProducerDTO;
import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import com.johan.weather_app_notification.dto.reciever.SubscriptionRecieverDTO;
import com.johan.weather_app_notification.rabbit.producer.AuthProducer;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import com.johan.weather_app_notification.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionEventListener {

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
        System.out.println("🎯 SUBSCRIPTION EVENT LISTENER TRIGGERED");
        System.out.println("📦 Received subscription DTO - userId: " + dto.userId() + ", city: " + dto.city());

        // Sätt globala variabler
        Globals.setGlobalUserId(dto.userId());
        Globals.setGlobalCity(dto.city());

        System.out.println("🌍 Globals set - userId: " + Globals.getGlobalUserId() + ", city: " + Globals.getGlobalCity());

        // Trigger weather data request
        try {
            System.out.println("🌤️ Sending weather data request for city: " + dto.city());
            weatherProducer.sendWeatherData(new WeatherProducerDTO(dto.city()));
            System.out.println("✅ Weather data request sent successfully");
        } catch (Exception e) {
            System.out.println("❌ Error sending weather data request: " + e.getMessage());
            e.printStackTrace();
        }


        // Trigger auth request för att hämta email
        try {
            System.out.println("🔐 Sending auth request for userId: " + dto.userId());
            WeatherAuthProducerDTO weatherAuthProducerDTO = new WeatherAuthProducerDTO(dto.userId());
            authProducer.getEmail(weatherAuthProducerDTO);
            System.out.println("✅ Auth request sent successfully");
        } catch (Exception e) {
            System.out.println("❌ Error sending auth request: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("🎉 Subscription event processing completed");
    }
}