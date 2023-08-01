package ru.practicum.explorewithme.util;

import java.time.format.DateTimeFormatter;

public final class Constants {

    private Constants() {
    }

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
