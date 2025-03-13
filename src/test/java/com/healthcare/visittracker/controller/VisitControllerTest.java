package com.healthcare.visittracker.controller;

import com.healthcare.visittracker.BaseTest;
import com.healthcare.visittracker.TestDataFactory;
import com.healthcare.visittracker.dto.request.VisitRequest;
import com.healthcare.visittracker.entity.Doctor;
import com.healthcare.visittracker.entity.Patient;
import com.healthcare.visittracker.entity.Visit;
import com.healthcare.visittracker.repository.DoctorRepository;
import com.healthcare.visittracker.repository.PatientRepository;
import com.healthcare.visittracker.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VisitControllerTest extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private VisitRepository visitRepository;

    private String baseUrl;
    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api";

        visitRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        doctor = doctorRepository.save(TestDataFactory.createDoctor());
        patient = patientRepository.save(TestDataFactory.createPatient());
    }

    @Test
    void shouldCreateVisit() {
        VisitRequest requestDto = TestDataFactory.createVisitRequest(doctor.getId(), patient.getId());

        ResponseEntity<Void> response = restTemplate.postForEntity(
                baseUrl + "/visits", requestDto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Visit> visits = visitRepository.findAll();
        assertThat(visits).hasSize(1);
        assertThat(visits.get(0).getDoctor().getId()).isEqualTo(doctor.getId());
        assertThat(visits.get(0).getPatient().getId()).isEqualTo(patient.getId());
    }

    @Test
    void shouldNotAllowOverlappingVisits() {
        ZonedDateTime[] timeSlot = TestDataFactory.createTimeSlot(doctor.getTimezone());

        Visit visit = TestDataFactory.createVisit(doctor, patient, timeSlot[0], timeSlot[1]);
        visitRepository.save(visit);

        Patient anotherPatient = patientRepository.save(
                TestDataFactory.createPatient("Bob", "White"));

        VisitRequest requestDto = TestDataFactory.createVisitRequest(
                doctor.getId(),
                anotherPatient.getId(),
                timeSlot[0].plusMinutes(30),
                timeSlot[1].plusMinutes(30));

        ResponseEntity<Object> response = restTemplate.postForEntity(
                baseUrl + "/visits", requestDto, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        List<Visit> visits = visitRepository.findAll();
        assertThat(visits).hasSize(1);
    }

    @Test
    void shouldHandleInvalidPatientId() {
        VisitRequest requestDto = TestDataFactory.createVisitRequest(
                doctor.getId(),
                999L); // Non-existent ID

        ResponseEntity<Object> response = restTemplate.postForEntity(
                baseUrl + "/visits", requestDto, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        List<Visit> visits = visitRepository.findAll();
        assertThat(visits).isEmpty();
    }

    @Test
    void shouldHandleInvalidTimeRange() {
        ZoneId doctorZone = ZoneId.of(doctor.getTimezone());

        ZonedDateTime start = ZonedDateTime.now(doctorZone)
                .plusDays(1).withHour(10).withMinute(0).withSecond(0);
        ZonedDateTime end = start.minusHours(1); // End is before start

        VisitRequest requestDto = TestDataFactory.createVisitRequest(
                doctor.getId(),
                patient.getId(),
                start,
                end);

        ResponseEntity<Object> response = restTemplate.postForEntity(
                baseUrl + "/visits", requestDto, Object.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        List<Visit> visits = visitRepository.findAll();
        assertThat(visits).isEmpty();
    }
}
