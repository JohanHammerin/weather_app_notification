package com.johan.weather_app_notification.service;


import com.johan.weather_app_notification.dto.reciever.SubscriptionRecieverDTO;
import com.johan.weather_app_notification.dto.reciever.WeatherRecieverDTO;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class WeatherNotificationService {

    private final MailSender mailSender;

    public WeatherNotificationService(MailSender mailSender) {
        this.mailSender = mailSender;
    }


    public String buildWeatherEmailContent(String city, WeatherRecieverDTO weatherDTO) {
        return String.format(
                "🌤️ Weather Update for %s 🌤️\n\n" +
                        "📅 Time: %s\n" +
                        "🌡️ Temperature: %.1f°C - %.1f°C\n" +
                        "☁️ Conditions: %s\n" +
                        "💧 Precipitation: %.1f mm\n\n" +
                        "Stay prepared and have a great day! 🌈\n\n" +
                        "Thank you for using our weather service!",
                city,
                weatherDTO.time(),
                weatherDTO.temperatureMin(),
                weatherDTO.temperatureMax(),
                weatherDTO.weatherStatus(),
                weatherDTO.precipitationSum()
        );
    }

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("moistusinc@gmail.com");
        mailSender.send(message);
    }
}
