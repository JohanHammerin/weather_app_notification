package com.johan.weather_app_notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.johan.weather_app_notification.dto.WeatherReceiverDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

@Service
public class WeatherEmailService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherEmailService.class);
    private static final String MAILJET_URL = "https://api.mailjet.com/v3.1/send";

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String secretKey;

    @Value("${mailjet.sender.email}")
    private String senderEmail;

    @Value("${mailjet.sender.name}")
    private String senderName;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // Används för att skapa säker JSON

    public WeatherEmailService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Skapar mailet och delegerar sändningen.
     * Tar emot hela DTO:n för att undvika redundanta parametrar.
     */
    public boolean sendWeatherNotification(String recipientName, WeatherReceiverDto dto) {
        String subject = String.format("🌤️ Weather Update for %s", dto.city());

        String htmlContent = buildWeatherHtmlEmail(recipientName, dto);
        String textContent = buildWeatherTextEmail(recipientName, dto);

        return sendMail(dto.email(), recipientName, subject, textContent, htmlContent);
    }

    /**
     * Bygger JSON-payload med Jackson och skickar via Mailjet API.
     */
    public boolean sendMail(String toEmail, String toName, String subject, String textPart, String htmlPart) {
        try {
            logger.info("Förbereder att skicka email via Mailjet till: {}", toEmail);

            // Bygg JSON säkert med Jackson (istället för String concatenation)
            ObjectNode root = objectMapper.createObjectNode();
            ArrayNode messages = root.putArray("Messages");

            ObjectNode message = messages.addObject();

            ObjectNode from = message.putObject("From");
            from.put("Email", senderEmail);
            from.put("Name", senderName);

            ArrayNode to = message.putArray("To");
            ObjectNode recipient = to.addObject();
            recipient.put("Email", toEmail);
            recipient.put("Name", toName);

            message.put("Subject", subject);
            message.put("TextPart", textPart);
            message.put("HTMLPart", htmlPart);

            String jsonBody = objectMapper.writeValueAsString(root);

            // Skapa Auth-header
            String authHeader = "Basic " + Base64.getEncoder()
                    .encodeToString((apiKey + ":" + secretKey).getBytes());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MAILJET_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authHeader)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                logger.info("Email skickat framgångsrikt till: {}", toEmail);
                return true;
            } else {
                logger.error("Mailjet API fel - Status: {}, Body: {}", response.statusCode(), response.body());
                return false;
            }

        } catch (Exception e) {
            logger.error("Kunde inte skicka email till: {}", toEmail, e);
            return false;
        }
    }

    private String buildWeatherHtmlEmail(String userName, WeatherReceiverDto dto) {
        // Jag har behållit din HTML-struktur men använder datan från DTO:n
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px; }
                    .content { background-color: #f9f9f9; padding: 20px; border-radius: 5px; margin-top: 20px; }
                    .weather-item { margin: 10px 0; padding: 10px; background-color: white; border-radius: 4px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .temperature-range { color: #3498db; }
                    .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>🌤️ Weather Update for %s</h1>
                    <p>Hello %s! Here's your weather report</p>
                </div>
                
                <div class="content">
                    <div class="weather-item">
                        <p><strong>📅 Time:</strong> %s</p>
                    </div>
                    
                    <div class="weather-item">
                        <p class="temperature-range">🌡️ Temperature: <strong>%.1f°C - %.1f°C</strong></p>
                    </div>
                    
                    <div class="weather-item">
                        <p><strong>☁️ Conditions:</strong> %s</p>
                    </div>
                    
                    <div class="weather-item">
                        <p><strong>💧 Precipitation:</strong> %.1f mm</p>
                    </div>
                </div>
                
                <div class="footer">
                    <p>Stay prepared and have a great day! 🌈</p>
                    <p>Thank you for using our weather service!</p>
                    <p>© 2024 Weather App Notification System.</p>
                </div>
            </body>
            </html>
            """, dto.city(), userName, dto.time(),
                dto.temperatureMin(), dto.temperatureMax(),
                dto.weatherStatus(), dto.precipitationSum());
    }

    private String buildWeatherTextEmail(String userName, WeatherReceiverDto dto) {
        return String.format(
                "🌤️ Weather Update for %s 🌤️\n\n" +
                        "Hello %s!\n\n" +
                        "📅 Time: %s\n" +
                        "🌡️ Temperature: %.1f°C - %.1f°C\n" +
                        "☁️ Conditions: %s\n" +
                        "💧 Precipitation: %.1f mm\n\n" +
                        "Stay prepared and have a great day! 🌈\n\n" +
                        "Thank you for using our weather service!\n" +
                        "© 2025 Moistus Inc",
                dto.city(), userName, dto.time(),
                dto.temperatureMin(), dto.temperatureMax(),
                dto.weatherStatus(), dto.precipitationSum());
    }
}