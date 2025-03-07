package com.healthcare.visittracker.controller;

import com.healthcare.visittracker.BaseTest;
import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.entity.Doctor;
import com.healthcare.visittracker.entity.Patient;
import com.healthcare.visittracker.entity.Visit;
import com.healthcare.visittracker.repository.DoctorRepository;
import com.healthcare.visittracker.repository.PatientRepository;
import com.healthcare.visittracker.repository.VisitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PatientControllerTest extends BaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private VisitRepository visitRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
        // Clean up database
        visitRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();

        // Setup test data
        setupTestData();
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        visitRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    private void setupTestData() {
        // Create doctors
        Doctor doctor1 = doctorRepository.save(Doctor.builder()
                .firstName("John")
                .lastName("Smith")
                .timezone("America/New_York")
                .build());

        Doctor doctor2 = doctorRepository.save(Doctor.builder()
                .firstName("Mary")
                .lastName("Johnson")
                .timezone("America/Chicago")
                .build());

        // Create patients
        Patient patient1 = patientRepository.save(Patient.builder()
                .firstName("Alice")
                .lastName("Brown")
                .build());

        Patient patient2 = patientRepository.save(Patient.builder()
                .firstName("Bob")
                .lastName("White")
                .build());

        Patient patient3 = patientRepository.save(Patient.builder()
                .firstName("Carol")
                .lastName("Davis")
                .build());

        // Create visits
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        // Visits for doctor1
        visitRepository.save(Visit.builder()
                .doctor(doctor1)
                .patient(patient1)
                .startDateTime(now.minusDays(5))
                .endDateTime(now.minusDays(5).plusHours(1))
                .build());

        visitRepository.save(Visit.builder()
                .doctor(doctor1)
                .patient(patient2)
                .startDateTime(now.minusDays(3))
                .endDateTime(now.minusDays(3).plusHours(1))
                .build());

        // Visits for doctor2
        visitRepository.save(Visit.builder()
                .doctor(doctor2)
                .patient(patient2)
                .startDateTime(now.minusDays(2))
                .endDateTime(now.minusDays(2).plusHours(1))
                .build());

        visitRepository.save(Visit.builder()
                .doctor(doctor2)
                .patient(patient3)
                .startDateTime(now.minusDays(1))
                .endDateTime(now.minusDays(1).plusHours(1))
                .build());
    }

    @Test
    void getAllPatientsWithNoFilters() {
        String url = baseUrl + "/patients";

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(3);
        assertThat(response.getBody().getCount()).isEqualTo(3);
    }

    @Test
    void getAllPatientsWithPagination() {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("page", 0)
                .queryParam("size", 2)
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(2);
        assertThat(response.getBody().getCount()).isEqualTo(3);
    }

    @Test
    void getAllPatientsWithSearch() {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("search", "Al") // Should match "Alice"
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);
        assertThat(response.getBody().getData().get(0).getFirstName()).isEqualTo("Alice");
    }

    @Test
    void getAllPatientsWithDoctorFilter() {
        Long doctor1Id = doctorRepository.findByFirstNameAndLastName("John", "Smith")
                .map(Doctor::getId)
                .orElseThrow();

        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("doctorIds", doctor1Id)
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(2); // Alice and Bob visited John Smith

        response.getBody().getData().forEach(patient -> {
            assertThat(patient.getLastVisits()).hasSize(1);
            assertThat(patient.getLastVisits().get(0).getDoctor().getFirstName()).isEqualTo("John");
        });
    }

    @Test
    void getAllPatientsWithCombinedFilters() {
        Long doctor2Id = doctorRepository.findByFirstNameAndLastName("Mary", "Johnson")
                .map(Doctor::getId)
                .orElseThrow();

        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("search", "o") // Matches "Bob" and "Carol"
                .queryParam("doctorIds", doctor2Id)
                .queryParam("page", 0)
                .queryParam("size", 1) // Limit to 1 result
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1); // Only one due to pagination
        assertThat(response.getBody().getCount()).isEqualTo(2); // Should match both Bob and Carol

        String firstName = response.getBody().getData().get(0).getFirstName();
        assertThat(firstName).isIn("Bob", "Carol");
    }
}
