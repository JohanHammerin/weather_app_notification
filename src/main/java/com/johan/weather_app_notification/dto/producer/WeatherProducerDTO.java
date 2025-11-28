package com.johan.weather_app_notification.dto.producer;

import java.util.UUID;

public record WeatherProducerDTO(
        String city,
        UUID userId  // ✅ Skicka userId istället för email
) {}