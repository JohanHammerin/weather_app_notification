package com.johan.weather_app_notification.dto;

public record WeatherReceiverDTO(
        String time,
        double temperatureMin,
        double temperatureMax,
        String weatherStatus,
        double precipitationSum,
        String city,
        String email
) {}