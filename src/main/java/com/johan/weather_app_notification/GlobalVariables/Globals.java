package com.johan.weather_app_notification.GlobalVariables;

import java.util.UUID;

public class Globals {
    public static UUID GLOBAL_USERID;
    public static String GLOBAL_EMAIL;
    public static String GLOBAL_CITY;


    public static UUID getGlobalUserid() {
        return GLOBAL_USERID;
    }

    public static void setGlobalUserid(UUID globalUserid) {
        GLOBAL_USERID = globalUserid;
    }

    public static String getGlobalEmail() {
        return GLOBAL_EMAIL;
    }

    public static void setGlobalEmail(String globalEmail) {
        GLOBAL_EMAIL = globalEmail;
    }

    public static String getGlobalCity() {
        return GLOBAL_CITY;
    }

    public static void setGlobalCity(String globalCity) {
        GLOBAL_CITY = globalCity;
    }
}
