package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter OFFSET_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static Instant parseToInstant(String dateTimeStr, ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, LOCAL_FORMATTER);
        return localDateTime.atZone(zoneId).toInstant();
    }

    public static String formatLocalDateTime(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).format(LOCAL_FORMATTER);
    }

    public static String formatWithOffset(Instant instant, ZoneId zoneId) {
        return instant.atZone(zoneId).format(OFFSET_FORMATTER);
    }

}
