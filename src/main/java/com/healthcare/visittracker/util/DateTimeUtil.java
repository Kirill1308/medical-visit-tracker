package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private static final ZoneId UTC = ZoneId.of("UTC");

    public static ZonedDateTime parseDateTime(String dateTimeStr) {
        ZonedDateTime dateTime = ZonedDateTime.parse(dateTimeStr, ISO_FORMATTER);
        return dateTime.withZoneSameInstant(UTC);
    }

    public static String formatDateTime(ZonedDateTime dateTime) {
        ZonedDateTime utcDateTime = dateTime.withZoneSameInstant(UTC);
        return utcDateTime.format(ISO_FORMATTER);
    }

    public static ZonedDateTime convertToTimezone(ZonedDateTime dateTime, String targetTimezone) {
        return dateTime.withZoneSameInstant(ZoneId.of(targetTimezone));
    }

    public static boolean periodsOverlap(ZonedDateTime start1, ZonedDateTime end1,
                                         ZonedDateTime start2, ZonedDateTime end2) {
        ZonedDateTime s1 = start1.withZoneSameInstant(UTC);
        ZonedDateTime e1 = end1.withZoneSameInstant(UTC);
        ZonedDateTime s2 = start2.withZoneSameInstant(UTC);
        ZonedDateTime e2 = end2.withZoneSameInstant(UTC);

        return s1.isBefore(e2) && e1.isAfter(s2);
    }
}
