package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.reciever.WeatherRecieverDTO;
import com.johan.weather_app_notification.service.MailService;
import com.johan.weather_app_notification.service.WeatherNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WeatherResponseListener {

    private static final Logger logger = LoggerFactory.getLogger(WeatherResponseListener.class);
    private final WeatherNotificationService weatherNotificationService;
    private final MailService mailService;

    @Autowired
    public WeatherResponseListener(WeatherNotificationService weatherNotificationService, MailService mailService) {
        this.weatherNotificationService = weatherNotificationService;
        this.mailService = mailService;
    }

    @RabbitListener(queues = RabbitConfig.WEATHER_WEATHER_RESPONSE_QUEUE)
    public void handleWeatherResponse(WeatherRecieverDTO weatherDTO) {
        try {
            logger.info("🌤️ Received weather data for userId: {}, city: {}", weatherDTO.userId(), weatherDTO.city());

            // Hämta email BASERAT PÅ userId från DTO
            String userEmail = getUserEmailFromAuthService(weatherDTO.userId());

            if (userEmail == null || userEmail.isEmpty()) {
                logger.error("❌ No email found for userId: {}", weatherDTO.userId());
                return;
            }

            System.out.println("🌤️ Weather Update:");
            System.out.println("   Time: " + weatherDTO.time());
            System.out.println("   Min temp: " + weatherDTO.temperatureMin() + "°C");
            System.out.println("   Max temp: " + weatherDTO.temperatureMax() + "°C");
            System.out.println("   Status: " + weatherDTO.weatherStatus());
            System.out.println("   Precipitation: " + weatherDTO.precipitationSum() + " mm");

            String content = weatherNotificationService.buildWeatherEmailContent(weatherDTO.city(), weatherDTO);

            logger.info("📧 Sending weather email to: {} for city: {}", userEmail, weatherDTO.city());
            mailService.sendMail(userEmail, "Vädernotis", content);

        } catch (Exception e) {
            logger.error("❌ Error handling weather response", e);
        }
    }

    private String getUserEmailFromAuthService(UUID userId) {
        try {
            // TEMPORÄR LÖSNING - hårdkoda mapping
            if (UUID.fromString("c6346d56-b8c2-4187-8a8c-a30826df1e29").equals(userId)) {
                return "tommy.haraldsson@stud.sti.se";
            } else if (UUID.fromString("16e9261f-7f32-4745-9604-1dd83d080343").equals(userId)) {
                return "alyckenius@gmail.com";
            }
            logger.warn("⚠️ No email mapping for userId: {}", userId);
            return null;
        } catch (Exception e) {
            logger.error("❌ Failed to get email for userId: {}", userId, e);
            return null;
        }
    }
}