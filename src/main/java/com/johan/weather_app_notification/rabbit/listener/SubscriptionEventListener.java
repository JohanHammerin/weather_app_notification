package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.config.RabbitConfig;
import com.johan.weather_app_notification.dto.UserIdCityDto;
import com.johan.weather_app_notification.rabbit.producer.AuthProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEventListener.class);
    private final AuthProducer authProducer;

    public SubscriptionEventListener(AuthProducer authProducer) {
        this.authProducer = authProducer;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_SUBSCRIPTION)
    public void handleSubscriptionEvent(UserIdCityDto dto) {
        logger.info("Mottog subscription-event för userId: {} och stad: {}. Skickar förfrågan till AuthProducer...",
                dto.userId(), dto.city());

        authProducer.sendAuthRequest(dto);

        logger.info("Förfrågan skickad till AuthProducer.");
    }
}