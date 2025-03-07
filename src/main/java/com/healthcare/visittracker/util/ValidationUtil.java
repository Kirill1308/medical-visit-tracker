package com.healthcare.visittracker.util;

import com.healthcare.visittracker.exception.custom.InvalidTimezoneException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    private static final Set<String> VALID_TIMEZONE_IDS = ZoneId.getAvailableZoneIds();

    public static void validateTimezone(String timezoneId) {
        if (!VALID_TIMEZONE_IDS.contains(timezoneId)) {
            throw new InvalidTimezoneException("Invalid timezone ID: " + timezoneId);
        }
    }

    public static boolean validateTimeRange(ZonedDateTime start, ZonedDateTime end) {
        return end.isAfter(start);
    }

    public static boolean isValidDateTimeForTimezone(String dateTimeStr, String timezoneId) {
        try {
            ZonedDateTime parsed = DateTimeUtil.parseDateTime(dateTimeStr);
            ZoneId zoneId = ZoneId.of(timezoneId);

            return parsed.getZone().equals(zoneId);
        } catch (DateTimeException e) {
            return false;
        }
    }
}
