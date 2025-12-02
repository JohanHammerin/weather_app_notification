package com.johan.weather_app_notification.dto;

/**
 * DTO som håller information om användarens email och vald stad.
 * Används för att transportera denna data genom systemet utan globala variabler.
 */
public record EmailCityDto(
        String email,
        String city
) {
}