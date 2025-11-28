package com.johan.weather_app_notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final String apiKey = "48db850aa133316a9ed3e9bdc1ac7784";
    private final String secretKey = "a4ae90102fb6ef239be31306d5e34943";

    private final HttpClient httpClient;

    public MailService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public void sendMail(String to, String subject, String text) {
        try {
            logger.info("📧 Sending email via Mailjet API to: {}", to);

            // Skapa JSON request body
            String jsonBody = String.format(
                    "{" +
                            "\"Messages\":[{" +
                            "\"From\":{\"Email\":\"moistusinc@gmail.com\",\"Name\":\"Weather App\"}," +
                            "\"To\":[{\"Email\":\"%s\"}]," +
                            "\"Subject\":\"%s\"," +
                            "\"TextPart\":\"%s\"" +
                            "}]" +
                            "}",
                    to,
                    subject,
                    text.replace("\"", "\\\"").replace("\n", "\\n")
            );

            logger.debug("📦 Mailjet API Request: {}", jsonBody);

            // Skapa HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mailjet.com/v3.1/send"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " +
                            Base64.getEncoder().encodeToString((apiKey + ":" + secretKey).getBytes()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            // Skicka request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            logger.info("📨 Mailjet API Response - Status: {}, Body: {}", response.statusCode(), response.body());

            if (response.statusCode() == 200) {
                logger.info("✅ Email sent successfully via Mailjet API to: {}", to);
            } else {
                logger.error("❌ Mailjet API error - Status: {}, Response: {}", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            logger.error("❌ Failed to send email via Mailjet API to: {}", to, e);
        }
    }
}