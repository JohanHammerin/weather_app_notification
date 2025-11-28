package com.johan.weather_app_notification.dto.reciever;

import java.util.UUID;

public record WeatherRecieverDTO(
        String time,
        double temperatureMin,
        double temperatureMax,
        String weatherStatus,
        double precipitationSum,
        UUID userId,        // ✅ Lägg till userId
        String city
) {}