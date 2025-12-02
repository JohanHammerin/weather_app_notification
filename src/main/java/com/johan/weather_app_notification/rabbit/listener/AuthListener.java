package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.EmailCityDto;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuthListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthListener.class);
    private final WeatherProducer weatherProducer;

    // @Autowired behövs inte här i moderna Spring-versioner, Spring fattar ändå!
    public AuthListener(WeatherProducer weatherProducer) {
        this.weatherProducer = weatherProducer;
    }

    /**
     * Lyssnar på auth-kön.
     * När en användare/stad kommer in här, skickar vi vidare förfrågan
     * till vädertjänsten för att hämta aktuell data.
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_AUTH_RESPONSE)
    public void processAuthRequest(EmailCityDto dto) {
        logger.info("Mottog auth-request för email: {} och stad: {}. Skickar vidare till WeatherProducer...",
                dto.email(), dto.city());

        weatherProducer.sendWeatherRequest(dto);

        logger.info("Data skickad till WeatherProducer.");
    }
}