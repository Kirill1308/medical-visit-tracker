package com.healthcare.visittracker;

import com.healthcare.visittracker.dto.request.VisitRequest;
import com.healthcare.visittracker.entity.Doctor;
import com.healthcare.visittracker.entity.Patient;
import com.healthcare.visittracker.entity.Visit;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TestDataFactory {

    private static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static Doctor createDoctor() {
        return Doctor.builder()
                .firstName("John")
                .lastName("Smith")
                .timezone("America/New_York")
                .build();
    }

    public static Doctor createDoctor(String firstName, String lastName, String timezone) {
        return Doctor.builder()
                .firstName(firstName)
                .lastName(lastName)
                .timezone(timezone)
                .build();
    }

    public static Patient createPatient() {
        return Patient.builder()
                .firstName("Alice")
                .lastName("Brown")
                .build();
    }

    public static Patient createPatient(String firstName, String lastName) {
        return Patient.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    public static VisitRequest createVisitRequest(Long doctorId, Long patientId) {
        ZonedDateTime start = ZonedDateTime.now(ZoneId.of("UTC"))
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0);
        ZonedDateTime end = start.plusHours(1);

        return VisitRequest.builder()
                .start(start.format(UTC_FORMATTER))
                .end(end.format(UTC_FORMATTER))
                .doctorId(doctorId)
                .patientId(patientId)
                .build();
    }

    public static VisitRequest createVisitRequest(Long doctorId, Long patientId, ZonedDateTime start, ZonedDateTime end) {
        return VisitRequest.builder()
                .start(start.withZoneSameInstant(ZoneId.of("UTC"))
                        .format(UTC_FORMATTER))
                .end(end.withZoneSameInstant(ZoneId.of("UTC"))
                        .format(UTC_FORMATTER))
                .doctorId(doctorId)
                .patientId(patientId)
                .build();
    }

    public static Visit createVisit(Doctor doctor, Patient patient) {
        ZonedDateTime start = ZonedDateTime.now(ZoneId.of(doctor.getTimezone()))
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0);
        ZonedDateTime end = start.plusHours(1);

        return Visit.builder()
                .doctor(doctor)
                .patient(patient)
                .startDateTime(start)
                .endDateTime(end)
                .build();
    }

    public static Visit createVisit(Doctor doctor, Patient patient, ZonedDateTime start, ZonedDateTime end) {
        return Visit.builder()
                .doctor(doctor)
                .patient(patient)
                .startDateTime(start)
                .endDateTime(end)
                .build();
    }

    public static ZonedDateTime[] createTimeSlot(String timezone) {
        ZonedDateTime start = ZonedDateTime.now(ZoneId.of(timezone))
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0);
        ZonedDateTime end = start.plusHours(1);

        return new ZonedDateTime[]{start, end};
    }

    public static ZonedDateTime[] createTimeSlot(String timezone, int daysOffset, int hour) {
        ZonedDateTime start = ZonedDateTime.now(ZoneId.of(timezone))
                .plusDays(daysOffset)
                .withHour(hour)
                .withMinute(0)
                .withSecond(0);
        ZonedDateTime end = start.plusHours(1);

        return new ZonedDateTime[]{start, end};
    }
}
