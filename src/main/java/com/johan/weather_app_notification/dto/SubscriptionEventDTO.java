package com.johan.weather_app_notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubscriptionEventDTO {

    @NotNull
    private long userId;

    @NotBlank
    private String city;

    public SubscriptionEventDTO() {}

    public SubscriptionEventDTO(long userId, String city) {
        this.userId = userId;
        this.city = city;
    }

}
