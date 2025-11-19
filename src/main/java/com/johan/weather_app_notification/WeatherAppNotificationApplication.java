package com.johan.weather_app_notification;

import com.johan.weather_app_notification.service.TestPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WeatherAppNotificationApplication {

    public static void main(String[] args) {
        // Få tag på context när applikationen startar
        ConfigurableApplicationContext context = SpringApplication.run(WeatherAppNotificationApplication.class, args);

        // Hämta din TestPublisher
        TestPublisher publisher = context.getBean(TestPublisher.class);

        // Skicka sample
        publisher.sendSample();
    }
}

