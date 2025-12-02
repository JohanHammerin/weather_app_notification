package com.johan.weather_app_notification.rabbit.listener;

import com.johan.weather_app_notification.dto.UserId_City_DTO;
import com.johan.weather_app_notification.rabbit.producer.AuthProducer;
import com.johan.weather_app_notification.rabbit.producer.WeatherProducer;
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


        @RabbitListener(queues = "weather.subscription.due")
        public void handleSubscriptionEvent(UserId_City_DTO dto) {
            logger.info("🎯 Subscription event listener triggered");
            logger.info("📋 Processing - userId: {}, city: {}", dto.userId(), dto.city());

            authProducer.getEmail(dto);



        }
    }