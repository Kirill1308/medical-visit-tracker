package com.healthcare.visittracker.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static boolean validateTimeRange(ZonedDateTime start, ZonedDateTime end) {
        return end.isAfter(start);
    }
}
