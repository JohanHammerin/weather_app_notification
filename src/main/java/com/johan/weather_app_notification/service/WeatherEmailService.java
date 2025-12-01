package com.johan.weather_app_notification.service;

import com.johan.weather_app_notification.dto.reciever.WeatherRecieverDTO;
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

    @Value("${mailjet.api.key:48db850aa133316a9ed3e9bdc1ac7784}")
    private String apiKey;

    @Value("${mailjet.api.secret:a4ae90102fb6ef239be31306d5e34943}")
    private String secretKey;

    @Value("${mailjet.sender.email:moistusinc@gmail.com}")
    private String senderEmail;

    @Value("${mailjet.sender.name:Weather App}")
    private String senderName;

    private final HttpClient httpClient;

    public WeatherEmailService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    // ✅ Här är den viktigaste metoden - kombinerar båda servicernas funktionalitet
    public boolean sendWeatherNotification(String recipientEmail, String recipientName,
                                           String city, WeatherRecieverDTO weatherDTO) {

        String subject = String.format("🌤️ Weather Update for %s", city);

        // Använd HTML-version för bättre presentation
        String htmlContent = buildWeatherHtmlEmail(recipientName, city, weatherDTO);
        String textContent = buildWeatherTextEmail(recipientName, city, weatherDTO);

        return sendMail(recipientEmail, recipientName, subject, textContent, htmlContent);
    }

    // Generisk metod för att skicka mail
    public boolean sendMail(String to, String toName, String subject, String textPart, String htmlPart) {
        try {
            logger.info("📧 Sending email via Mailjet API to: {}", to);

            String jsonBody = String.format(
                    "{" +
                            "\"Messages\":[{" +
                            "\"From\":{\"Email\":\"%s\",\"Name\":\"%s\"}," +
                            "\"To\":[{\"Email\":\"%s\",\"Name\":\"%s\"}]," +
                            "\"Subject\":\"%s\"," +
                            "\"TextPart\":\"%s\"," +
                            "\"HTMLPart\":\"%s\"" +
                            "}]" +
                            "}",
                    senderEmail,
                    senderName,
                    to,
                    toName,
                    escapeJson(subject),
                    escapeJson(textPart),
                    escapeJson(htmlPart)
            );

            logger.debug("📦 Mailjet API Request: {}", jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mailjet.com/v3.1/send"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " +
                            Base64.getEncoder().encodeToString((apiKey + ":" + secretKey).getBytes()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("📨 Mailjet API Response - Status: {}", response.statusCode());

            if (response.statusCode() == 200) {
                logger.info("✅ Email sent successfully via Mailjet API to: {}", to);
                return true;
            } else {
                logger.error("❌ Mailjet API error - Status: {}, Response: {}", response.statusCode(), response.body());
                return false;
            }

        } catch (Exception e) {
            logger.error("❌ Failed to send email via Mailjet API to: {}", to, e);
            return false;
        }
    }

    // ✅ Här tar vi med den bra mailbyggnaden från WeatherNotificationService
    private String buildWeatherHtmlEmail(String userName, String city, WeatherRecieverDTO weatherDTO) {
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
                    .temperature { color: #e74c3c; font-size: 24px; font-weight: bold; }
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
            """, city, userName, weatherDTO.time(),
                weatherDTO.temperatureMin(), weatherDTO.temperatureMax(),
                weatherDTO.weatherStatus(), weatherDTO.precipitationSum());
    }

    private String buildWeatherTextEmail(String userName, String city, WeatherRecieverDTO weatherDTO) {
        return String.format(
                "🌤️ Weather Update for %s 🌤️\\n\\n" +
                        "Hello %s!\\n\\n" +
                        "📅 Time: %s\\n" +
                        "🌡️ Temperature: %.1f°C - %.1f°C\\n" +
                        "☁️ Conditions: %s\\n" +
                        "💧 Precipitation: %.1f mm\\n\\n" +
                        "Stay prepared and have a great day! 🌈\\n\\n" +
                        "Thank you for using our weather service!\\n" +
                        "© 2024 Weather App",
                city, userName, weatherDTO.time(),
                weatherDTO.temperatureMin(), weatherDTO.temperatureMax(),
                weatherDTO.weatherStatus(), weatherDTO.precipitationSum());
    }

    // ✅ Enkel metod för att bara skicka textmail (om du behöver det)
    public boolean sendSimpleMail(String to, String subject, String text) {
        return sendMail(to, to.split("@")[0], subject, text, text);
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}