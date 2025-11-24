package com.johan.weather_app_notification.dto.reciever;

public record WeatherRecieverDTO(
        String time,
        double temperatureMin,
        double temperatureMax,
        String weatherStatus,
        double precipitationSum

) {
}
