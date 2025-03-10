package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    private static final DateTimeFormatter LOCAL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final ZoneId UTC = ZoneId.of("UTC");

    public static ZonedDateTime parseDateTime(String dateTimeStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, LOCAL_FORMATTER);
        return ZonedDateTime.of(localDateTime, UTC);
    }

    public static String formatDateTime(ZonedDateTime dateTime) {
        ZonedDateTime utcDateTime = dateTime.withZoneSameInstant(UTC);
        return utcDateTime.format(LOCAL_FORMATTER);
    }
}
