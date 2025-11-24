package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.Globals;
import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.reciever.WeatherRecieverDTO;
import com.johan.weather_app_notification.service.MailService;
import com.johan.weather_app_notification.service.WeatherNotificationService;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            logger.info("✅ Received weather response for time: {}", weatherDTO.time());


            System.out.println("🌤️ Weather Update:");
            System.out.println("   Time: " + weatherDTO.time());
            System.out.println("   Min temp: " + weatherDTO.temperatureMin() + "°C");
            System.out.println("   Max temp: " + weatherDTO.temperatureMax() + "°C");
            System.out.println("   Status: " + weatherDTO.weatherStatus());
            System.out.println("   Precipitation: " + weatherDTO.precipitationSum() + " mm");


            String to = "tommy.haraldsson@stud.sti.se";
            String content = weatherNotificationService.buildWeatherEmailContent(Globals.getGlobalCity(), weatherDTO);
            mailService.sendMail(to, "Vädernotis", content);


        } catch (Exception e) {
            logger.error("Error handling weather response", e);
        }
    }
}
