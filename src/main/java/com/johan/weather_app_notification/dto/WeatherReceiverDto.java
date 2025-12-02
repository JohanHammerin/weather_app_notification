package com.johan.weather_app_notification.dto;

/**
 * DTO som tar emot väderdata från Weather-tjänsten.
 * Innehåller väderinformation samt e-postadressen dit datan ska skickas.
 */
public record WeatherReceiverDto(
        String time,
        double temperatureMin,
        double temperatureMax,
        String weatherStatus,
        double precipitationSum,
        String city,
        String email
) {
}