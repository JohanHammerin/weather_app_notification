package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.GlobalVariables.Globals;
import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.reciever.WeatherRecieverDTO;
import com.johan.weather_app_notification.service.WeatherEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherResponseListener {

    private static final Logger logger = LoggerFactory.getLogger(WeatherResponseListener.class);

    // Använd WeatherEmailService istället för två separata services
    private final WeatherEmailService weatherEmailService;

    @Autowired
    public WeatherResponseListener(WeatherEmailService weatherEmailService) {
        this.weatherEmailService = weatherEmailService;
    }

    @RabbitListener(queues = RabbitConfig.WEATHER_WEATHER_RESPONSE_QUEUE)
    public void handleWeatherResponse(WeatherRecieverDTO weatherDTO) {
        try {
            String userEmail = Globals.GLOBAL_EMAIL;
            String city = Globals.GLOBAL_CITY;

            logger.info("🌤️ Received weather data for user: {}, city: {}", userEmail, city);

            // Logga väderdata
            logger.info("Weather details for {}:", city);
            logger.info("   📅 Time: {}", weatherDTO.time());
            logger.info("   🌡️ Temperature: {:.1f}°C - {:.1f}°C",
                    weatherDTO.temperatureMin(), weatherDTO.temperatureMax());
            logger.info("   ☁️ Status: {}", weatherDTO.weatherStatus());
            logger.info("   💧 Precipitation: {:.1f} mm", weatherDTO.precipitationSum());

            // Skicka vädernotifikation med HTML-format
            String recipientName = extractNameFromEmail(userEmail);
            boolean emailSent = weatherEmailService.sendWeatherNotification(
                    userEmail,
                    recipientName,
                    city,
                    weatherDTO
            );

            if (emailSent) {
                logger.info("✅ Weather notification successfully sent to: {}", userEmail);
            } else {
                logger.error("❌ Failed to send weather notification to: {}", userEmail);
            }

        } catch (Exception e) {
            logger.error("❌ Error handling weather response", e);
        }
    }

    // Hjälpmetod för att extrahera namn från email
    private String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Weather User";
        }

        // Ta bort @-domänen och ersätt punkt med mellanslag
        String namePart = email.split("@")[0];

        // Kapitalisera första bokstaven
        if (namePart.length() > 1) {
            return namePart.substring(0, 1).toUpperCase() +
                    namePart.substring(1).toLowerCase();
        }

        return namePart;
    }
}