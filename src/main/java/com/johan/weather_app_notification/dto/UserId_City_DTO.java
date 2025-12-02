package com.johan.weather_app_notification.dto;

import java.util.UUID;

public record UserId_City_DTO(
        UUID userId,
        String city
) {
}
