package ru.practicum.explorewithme.util;

import java.time.format.DateTimeFormatter;

public final class Constants {

    private Constants() {
    }

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);

}
