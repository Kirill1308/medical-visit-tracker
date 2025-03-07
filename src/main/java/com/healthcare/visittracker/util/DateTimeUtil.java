package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    public static ZonedDateTime parseDateTime(String dateTimeStr) {
        return ZonedDateTime.parse(dateTimeStr, ISO_FORMATTER);
    }

    public static String formatDateTime(ZonedDateTime dateTime) {
        return dateTime.format(ISO_FORMATTER);
    }

    public static ZonedDateTime convertToTimezone(ZonedDateTime dateTime, String targetTimezone) {
        return dateTime.withZoneSameInstant(ZoneId.of(targetTimezone));
    }

    public static boolean periodsOverlap(ZonedDateTime start1, ZonedDateTime end1,
                                         ZonedDateTime start2, ZonedDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
