package com.johan.weather_app_notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubscriptionEventDTO(

        @NotNull
        long userId,

        @NotBlank
        String city
) {
}
