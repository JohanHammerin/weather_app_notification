package com.johan.weather_app_notification;

import java.util.UUID;

public class Globals {
    private static final ThreadLocal<UUID> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> CITY = new ThreadLocal<>();
    private static final ThreadLocal<String> EMAIL = new ThreadLocal<>();

    public static String getGlobalEmail() {
        return EMAIL.get();
    }

    public static void setGlobalEmail(String globalEmail) {
        EMAIL.set(globalEmail);
    }

    public static UUID getGlobalUserId() {
        return USER_ID.get();
    }

    public static void setGlobalUserId(UUID globalUserId) {
        USER_ID.set(globalUserId);
    }

    public static String getGlobalCity() {
        return CITY.get();
    }

    public static void setGlobalCity(String globalCity) {
        CITY.set(globalCity);
    }

    // Glöm inte att rensa efter användning!
    public static void clear() {
        USER_ID.remove();
        CITY.remove();
        EMAIL.remove();
    }
}