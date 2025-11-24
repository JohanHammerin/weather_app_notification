package com.johan.weather_app_notification;

import java.util.UUID;

public class Globals {
    static UUID GLOBAL_USER_ID;
    static String GLOBAL_CITY;
    static String GLOBAL_EMAIL;

    public static String getGlobalEmail() {
        return GLOBAL_EMAIL;
    }

    public static void setGlobalEmail(String globalEmail) {
        GLOBAL_EMAIL = globalEmail;
    }

    public static UUID getGlobalUserId() {
        return GLOBAL_USER_ID;
    }

    public static void setGlobalUserId(UUID globalUserId) {
        GLOBAL_USER_ID = globalUserId;
    }

    public static String getGlobalCity() {
        return GLOBAL_CITY;
    }

    public static void setGlobalCity(String globalCity) {
        GLOBAL_CITY = globalCity;
    }
}
