package ru.practicum.explorewithme.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;

public final class Constants {

    private Constants() {
    }

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static String APPLICATION_NAME;

    @Value("${app.name}")
    public void setAppName(String name) {
        Constants.APPLICATION_NAME = name;
    }

    public static String getAppName() {
        return APPLICATION_NAME;
    }

}
