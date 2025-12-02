package com.johan.weather_app_notification.dto;

import java.util.UUID;

/**
 * DTO som kopplar ihop ett användar-ID med en stad.
 * Används för att skicka förfrågningar genom systemet utan att tappa bort vem datan tillhör.
 */
public record UserIdCityDto(
        UUID userId,
        String city
) {
}