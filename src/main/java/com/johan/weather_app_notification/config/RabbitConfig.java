package com.johan.weather_app_notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguration för RabbitMQ.
 * Sätter upp Exchange, Köer och Routing Keys samt hanterar JSON-serialisering.
 */
@Configuration
public class RabbitConfig {

    // --- Exchange Name ---
    public static final String EXCHANGE_WEATHER = "weather-exchange";

    // --- Queue Names ---
    // Namngivning optimerad för att gruppera alla köer under prefixet "QUEUE_"
    public static final String QUEUE_WEATHER_REQUEST = "weather-request-queue";
    public static final String QUEUE_WEATHER_RESPONSE = "weather-response-queue";
    public static final String QUEUE_MAIL = "weather-mail-queue";
    public static final String QUEUE_SUBSCRIPTION = "weather.subscription.due";
    public static final String QUEUE_AUTH_REQUEST = "auth-request-queue";
    public static final String QUEUE_AUTH_RESPONSE = "auth-response-queue";

    // --- Routing Keys ---
    public static final String ROUTING_KEY_WEATHER_REQUEST = "weather.request";
    public static final String ROUTING_KEY_WEATHER_RESPONSE = "weather.response";
    public static final String ROUTING_KEY_AUTH_REQUEST = "auth.request";
    public static final String ROUTING_KEY_AUTH_RESPONSE = "auth.response";
    public static final String ROUTING_KEY_MAIL = "weather.mail";
    public static final String ROUTING_KEY_SUBSCRIPTION = "weather.subscription";

    /**
     * Konverterare som gör att vi kan skicka Java-objekt (DTOs) som automatiskt
     * omvandlas till JSON i meddelandena.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setAlwaysConvertToInferredType(false);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    // --- Exchange Definition ---

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_WEATHER);
    }

    // --- Queue Definitions ---

    @Bean
    public Queue requestQueue() {
        return new Queue(QUEUE_WEATHER_REQUEST, true); // true = durable (kön överlever om RabbitMQ startas om)
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(QUEUE_WEATHER_RESPONSE, true);
    }

    @Bean
    public Queue weatherMailQueue() {
        return new Queue(QUEUE_MAIL, true);
    }

    @Bean
    public Queue weatherSubscriptionQueue() {
        return new Queue(QUEUE_SUBSCRIPTION, true);
    }

    @Bean
    public Queue authRequestQueue() {
        return new Queue(QUEUE_AUTH_REQUEST, true);
    }

    @Bean
    public Queue authResponseQueue() {
        return new Queue(QUEUE_AUTH_RESPONSE, true);
    }

    // --- Bindings (Kopplar köer till Exchange med Routing Keys) ---

    @Bean
    public Binding requestBinding() {
        return BindingBuilder.bind(requestQueue())
                .to(exchange())
                .with(ROUTING_KEY_WEATHER_REQUEST);
    }

    @Bean
    public Binding responseBinding() {
        return BindingBuilder.bind(responseQueue())
                .to(exchange())
                .with(ROUTING_KEY_WEATHER_RESPONSE);
    }

    @Bean
    public Binding weatherMailBinding() {
        return BindingBuilder.bind(weatherMailQueue())
                .to(exchange())
                .with(ROUTING_KEY_MAIL);
    }

    @Bean
    public Binding weatherSubscriptionBinding() {
        return BindingBuilder.bind(weatherSubscriptionQueue())
                .to(exchange())
                .with(ROUTING_KEY_SUBSCRIPTION);
    }

    @Bean
    public Binding authRequestBinding() {
        return BindingBuilder.bind(authRequestQueue())
                .to(exchange())
                .with(ROUTING_KEY_AUTH_REQUEST);
    }

    @Bean
    public Binding authResponseBinding() {
        return BindingBuilder.bind(authResponseQueue())
                .to(exchange())
                .with(ROUTING_KEY_AUTH_RESPONSE);
    }
}