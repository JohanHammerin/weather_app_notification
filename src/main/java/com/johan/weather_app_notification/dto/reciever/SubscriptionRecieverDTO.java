package com.johan.weather_app_notification.dto.reciever;

import java.util.UUID;

public record SubscriptionRecieverDTO(
        UUID userId,
        String city
) {}