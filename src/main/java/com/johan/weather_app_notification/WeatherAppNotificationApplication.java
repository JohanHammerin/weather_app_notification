package com.johan.weather_app_notification;

import com.johan.weather_app_notification.dto.producer.WeatherProducerDTO;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableRabbit
@SpringBootApplication
public class WeatherAppNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherAppNotificationApplication.class, args);
    }
/*
    @Bean
    public CommandLineRunner testRabbitMQ(WeatherProducer weatherProducer) {
        return args -> {
            System.out.println("🚀 Testing RabbitMQ Exchange Configuration with LavinMQ...");

            // Testa weather producer
            WeatherProducerDTO city = new WeatherProducerDTO("Stcklm");
            weatherProducer.sendWeatherData(city);

            System.out.println("✅ Weather data sent to exchange!");
            System.out.println("📊 Check LavinMQ dashboard to verify");
        };
    }

 */
}