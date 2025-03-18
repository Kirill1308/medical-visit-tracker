package com.healthcare.visittracker.controller;

import com.healthcare.visittracker.BaseTest;
import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
import com.healthcare.visittracker.entity.Doctor;
import com.healthcare.visittracker.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PatientControllerTest extends BaseTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DoctorRepository doctorRepository;

    private String baseUrl;
    private Long doctor1Id;
    private Long doctor2Id;
    private Long doctor3Id;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api";

        doctor1Id = doctorRepository.findByFirstNameAndLastName("John", "Smith")
                .map(Doctor::getId)
                .orElseThrow(() -> new RuntimeException("Test data not properly initialized"));

        doctor2Id = doctorRepository.findByFirstNameAndLastName("Mary", "Johnson")
                .map(Doctor::getId)
                .orElseThrow(() -> new RuntimeException("Test data not properly initialized"));

        doctor3Id = doctorRepository.findByFirstNameAndLastName("David", "Lee")
                .map(Doctor::getId)
                .orElseThrow(() -> new RuntimeException("Test data not properly initialized"));
    }

    @Test
    void shouldReturnOrderedVisitsPerPatient() {
        String url = baseUrl + "/patients";

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Find Alice and check her visits
        Optional<PatientResponse> aliceOpt = response.getBody().getData().stream()
                .filter(p -> p.getFirstName().equals("Alice"))
                .findFirst();

        assertThat(aliceOpt).isPresent();
        PatientResponse alice = aliceOpt.get();

        assertThat(alice.getLastVisits()).hasSize(3);
        assertThat(alice.getLastVisits().get(0).getDoctor().getFirstName()).isEqualTo("John");
        assertThat(alice.getLastVisits().get(1).getDoctor().getFirstName()).isEqualTo("Mary");
        assertThat(alice.getLastVisits().get(2).getDoctor().getFirstName()).isEqualTo("David");

        // Find Bob and check his visits
        Optional<PatientResponse> bobOpt = response.getBody().getData().stream()
                .filter(p -> p.getFirstName().equals("Bob"))
                .findFirst();

        assertThat(bobOpt).isPresent();
        PatientResponse bob = bobOpt.get();

        assertThat(bob.getLastVisits()).hasSize(2);
        assertThat(bob.getLastVisits().get(0).getDoctor().getFirstName()).isEqualTo("Mary");
        assertThat(bob.getLastVisits().get(1).getDoctor().getFirstName()).isEqualTo("Mary");

        // The first visit's date should be more recent than the second
        LocalDateTime firstVisitDate = LocalDateTime.parse(bob.getLastVisits().get(0).getStart(), formatter);
        LocalDateTime secondVisitDate = LocalDateTime.parse(bob.getLastVisits().get(1).getStart(), formatter);
        assertThat(firstVisitDate).isAfter(secondVisitDate);
    }

    @Test
    void shouldReturnCorrectDoctorTotalPatients() {
        String url = baseUrl + "/patients";

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<PatientResponse> patients = response.getBody().getData();

        // Find Alice's visits to verify doctor counts
        Optional<PatientResponse> aliceOpt = patients.stream()
                .filter(p -> p.getFirstName().equals("Alice"))
                .findFirst();

        assertThat(aliceOpt).isPresent();
        PatientResponse alice = aliceOpt.get();

        // Check doctor counts in Alice's visits

        // Find John's visit and check count
        Optional<Integer> doctor1Patients = alice.getLastVisits().stream()
                .filter(v -> v.getDoctor().getFirstName().equals("John"))
                .map(v -> v.getDoctor().getTotalPatients())
                .findFirst();

        assertThat(doctor1Patients).contains(1);

        // Find Mary's visit and check count
        Optional<Integer> doctor2Patients = alice.getLastVisits().stream()
                .filter(v -> v.getDoctor().getFirstName().equals("Mary"))
                .map(v -> v.getDoctor().getTotalPatients())
                .findFirst();

        assertThat(doctor2Patients).contains(2);

        // Find David's visit and check count
        Optional<Integer> doctor3Patients = alice.getLastVisits().stream()
                .filter(v -> v.getDoctor().getFirstName().equals("David"))
                .map(v -> v.getDoctor().getTotalPatients())
                .findFirst();

        assertThat(doctor3Patients).contains(2);
    }

    @Test
    void shouldHandleMultipleDoctorIdsFilter() {
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("doctorIds", doctor1Id + "," + doctor3Id)
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        assertThat(response.getBody().getData()).hasSize(2);

        // For Alice, should have visits with both doctor1 and doctor3
        Optional<PatientResponse> aliceOpt = response.getBody().getData().stream()
                .filter(p -> p.getFirstName().equals("Alice"))
                .findFirst();

        assertThat(aliceOpt).isPresent();
        PatientResponse alice = aliceOpt.get();

        // Alice should have 2 visits (one with John, one with David)
        assertThat(alice.getLastVisits()).hasSize(2);

        // Verify doctor names are either John (doctor1) or David (doctor3)
        List<String> doctorNames = alice.getLastVisits().stream()
                .map(v -> v.getDoctor().getFirstName())
                .toList();

        assertThat(doctorNames).containsExactlyInAnyOrder("John", "David");
    }

    @Test
    void shouldHandlePatientWithNoVisits() {
        // There should be one patient (Dan) with no visits
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("search", "Dan")
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).hasSize(1);

        PatientResponse patient = response.getBody().getData().get(0);
        assertThat(patient.getFirstName()).isEqualTo("Dan");
        assertThat(patient.getLastVisits()).isEmpty();
    }

    @Test
    void shouldHandleComplexSearchCriteria() {
        // Search for patients with 'a' in their name who visited doctor2 or doctor3
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("search", "a")
                .queryParam("doctorIds", doctor2Id + "," + doctor3Id)
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Should match Alice (visited doctor2) and Carol (visited doctor3)
        assertThat(response.getBody().getData()).hasSize(2);

        List<String> names = response.getBody().getData().stream()
                .map(PatientResponse::getFirstName)
                .toList();

        assertThat(names).containsExactlyInAnyOrder("Alice", "Carol");
    }

    @Test
    void shouldHandleEmptyResults() {
        // Search for a non-existent patient
        String url = UriComponentsBuilder.fromUriString(baseUrl + "/patients")
                .queryParam("search", "NonExistent")
                .toUriString();

        ResponseEntity<PatientListResponse> response = restTemplate.getForEntity(
                url, PatientListResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isEmpty();
        assertThat(response.getBody().getCount()).isZero();
    }
}
