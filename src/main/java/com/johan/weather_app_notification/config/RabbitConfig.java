package com.johan.weather_app_notification.config;

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
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitConfig {

    // Exchange
    public static final String WEATHER_EXCHANGE = "weather-exchange";

    // Queues
    public static final String WEATHER_WEATHER_REQUEST_QUEUE = "weather-request-queue";
    public static final String WEATHER_WEATHER_RESPONSE_QUEUE = "weather-response-queue";
    public static final String WEATHER_MAIL_QUEUE = "weather-mail-queue";
    public static final String WEATHER_SUBSCRIPTION_QUEUE = "weather.subscription.due";
    public static final String WEATHER_AUTH_REQUEST_QUEUE = "auth-request-queue";
    public static final String WEATHER_AUTH_RESPONSE_QUEUE = "auth-response-queue";

    // Routing keys
    public static final String WEATHER_WEATHER_REQUEST_ROUTING_KEY = "weather.request";
    public static final String WEATHER_WEATHER_RESPONSE_ROUTING_KEY = "weather.response";
    public static final String WEATHER_AUTH_REQUEST_ROUTING_KEY = "auth.request";
    public static final String WEATHER_AUTH_RESPONSE_ROUTING_KEY = "auth.response";
    public static final String WEATHER_MAIL_ROUTING_KEY = "weather.mail";
    public static final String WEATHER_SUBSCRIPTION_ROUTING_KEY = "weather.subscription";

    // ✅ FIX: Custom MessageConverter som ignorerar type headers
    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Configure ObjectMapper as needed
        objectMapper.findAndRegisterModules();

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);

        converter.setAlwaysConvertToInferredType(false); // Default behavior

        return converter;
    }

    // ✅ RABBIT TEMPLATE
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // ✅ LISTENER FACTORY
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    // ✅ QUEUES
    @Bean
    public Queue requestQueue() {
        return new Queue(WEATHER_WEATHER_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(WEATHER_WEATHER_RESPONSE_QUEUE, true);
    }

    @Bean
    public Queue weatherMailQueue() {
        return new Queue(WEATHER_MAIL_QUEUE, true);
    }

    @Bean
    public Queue weatherSubscriptionQueue() {
        return new Queue(WEATHER_SUBSCRIPTION_QUEUE, true);
    }

    // ✅ AUTH QUEUES - NYA
    @Bean
    public Queue authRequestQueue() {
        return new Queue(WEATHER_AUTH_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue authResponseQueue() {
        return new Queue(WEATHER_AUTH_RESPONSE_QUEUE, true);
    }

    // ✅ EXCHANGE
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(WEATHER_EXCHANGE);
    }

    // ✅ BINDINGS
    @Bean
    public Binding requestBinding() {
        return BindingBuilder
                .bind(requestQueue())
                .to(exchange())
                .with(WEATHER_WEATHER_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding responseBinding() {
        return BindingBuilder
                .bind(responseQueue())
                .to(exchange())
                .with(WEATHER_WEATHER_RESPONSE_ROUTING_KEY);
    }

    @Bean
    public Binding weatherMailBinding() {
        return BindingBuilder
                .bind(weatherMailQueue())
                .to(exchange())
                .with(WEATHER_MAIL_ROUTING_KEY);
    }

    @Bean
    public Binding weatherSubscriptionBinding() {
        return BindingBuilder
                .bind(weatherSubscriptionQueue())
                .to(exchange())
                .with(WEATHER_SUBSCRIPTION_ROUTING_KEY);
    }

    // ✅ AUTH BINDINGS - NYA
    @Bean
    public Binding authRequestBinding() {
        return BindingBuilder
                .bind(authRequestQueue())
                .to(exchange())
                .with(WEATHER_AUTH_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding authResponseBinding() {
        return BindingBuilder
                .bind(authResponseQueue())
                .to(exchange())
                .with(WEATHER_AUTH_RESPONSE_ROUTING_KEY);
    }
}