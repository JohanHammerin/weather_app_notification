package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.Globals;
import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.reciever.WeatherRecieverDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
/*
public class AuthListener {


    @RabbitListener(queues = RabbitConfig.WEATHER_AUTH_RESPONSE_QUEUE)
    public void handleWeatherResponse(WeatherRecieverDTO weatherDTO) {
        try {


            System.out.println("🌤️ Weather Update:");
            System.out.println("   Time: " + weatherDTO.time());
            System.out.println("   Min temp: " + weatherDTO.temperatureMin() + "°C");
            System.out.println("   Max temp: " + weatherDTO.temperatureMax() + "°C");
            System.out.println("   Status: " + weatherDTO.weatherStatus());
            System.out.println("   Precipitation: " + weatherDTO.precipitationSum() + " mm");


            String to = Globals.getGlobalEmail();
            String content = weatherNotificationService.buildWeatherEmailContent(Globals.getGlobalCity(), weatherDTO);
            mailService.sendMail(to, "Vädernotis", content);


        } catch (Exception e) {
            logger.error("Error handling weather response", e);
        }
    }
}


 */