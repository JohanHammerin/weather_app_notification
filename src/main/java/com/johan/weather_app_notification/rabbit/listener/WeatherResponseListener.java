package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.WeatherReceiverDto;
import com.johan.weather_app_notification.service.WeatherEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WeatherResponseListener {

    private static final Logger logger = LoggerFactory.getLogger(WeatherResponseListener.class);
    private final WeatherEmailService weatherEmailService;

    // Ingen @Autowired behövs
    public WeatherResponseListener(WeatherEmailService weatherEmailService) {
        this.weatherEmailService = weatherEmailService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_WEATHER_RESPONSE)
    public void handleWeatherResponse(WeatherReceiverDto dto) {
        try {
            logger.info("Mottog väderdata för user: {} och stad: {}", dto.email(), dto.city());

            // Extrahera namn (logik för fallback om namn saknas i systemet)
            String recipientName = extractNameFromEmail(dto.email());

            // Anropa servicen för att skicka mailet
            boolean emailSent = weatherEmailService.sendWeatherNotification(recipientName, dto);

            if (emailSent) {
                logger.info("Vädernotifikation skickad till: {}", dto.email());
            } else {
                logger.error("Misslyckades att skicka vädernotifikation till: {}", dto.email());
            }

        } catch (Exception e) {
            // Logga hela stacktrace så ni ser varför det kraschade
            logger.error("Fel vid hantering av väder-response för email: {}", dto.email(), e);
        }
    }

    /**
     * Hjälpmetod för att gissa namn baserat på email.
     * Exempel: "johan.andersson@gmail.com" -> "Johan"
     */
    private String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Weather User";
        }

        String namePart = email.split("@")[0];

        // Om namnet har punkt (johan.andersson), ta bara förnamnet för enkelhetens skull
        if (namePart.contains(".")) {
            namePart = namePart.split("\\.")[0];
        }

        if (namePart.length() > 1) {
            return namePart.substring(0, 1).toUpperCase() + namePart.substring(1).toLowerCase();
        }

        return namePart;
    }
}