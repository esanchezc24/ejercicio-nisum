package com.exercise.nisum.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter format_response = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String formatDate(LocalDateTime date) {
        return formatDate(date, null);
    }

    public static String formatDate(LocalDateTime date, String format) {
        if (date == null) return null;
        DateTimeFormatter dateTimeFormatter = format != null ? DateTimeFormatter.ofPattern(format) : format_response;
        return date.format(dateTimeFormatter);
    }
}
