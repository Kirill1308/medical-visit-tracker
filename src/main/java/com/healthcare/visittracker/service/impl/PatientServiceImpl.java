package com.healthcare.visittracker.service.impl;

import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
import com.healthcare.visittracker.dto.response.VisitResponse;
import com.healthcare.visittracker.entity.Patient;
import com.healthcare.visittracker.entity.Visit;
import com.healthcare.visittracker.repository.PatientRepository;
import com.healthcare.visittracker.service.PatientService;
import com.healthcare.visittracker.util.MapperUtil;
import com.healthcare.visittracker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientListResponse getAllPatients(Integer page, Integer size, String search, List<Long> doctorIds) {
        Pageable pageable = PaginationUtil.createPageRequest(page, size);

        Page<Patient> patientsPage = getPaginatedPatients(search, doctorIds, pageable);
        long totalCount = patientsPage.getTotalElements();

        List<Long> patientIds = extractPatientIds(patientsPage);

        if (patientIds.isEmpty()) {
            return MapperUtil.toPatientListResponse(List.of(), totalCount);
        }

        List<Patient> patientsWithData = loadPatientsWithRelatedData(patientIds);
        Map<Long, Integer> doctorPatientCounts = patientRepository.getDoctorPatientCounts();

        List<PatientResponse> patientResponses = buildPatientResponses(patientsWithData, doctorPatientCounts, doctorIds);

        return MapperUtil.toPatientListResponse(patientResponses, totalCount);
    }

    private Page<Patient> getPaginatedPatients(String search, List<Long> doctorIds, Pageable pageable) {
        if (doctorIds == null || doctorIds.isEmpty()) {
            return patientRepository.findAllWithFilters(search, pageable);
        } else {
            return patientRepository.findAllByDoctorIdsAndSearch(doctorIds, search, pageable);
        }
    }

    private List<Long> extractPatientIds(Page<Patient> patientsPage) {
        return patientsPage.getContent().stream()
                .map(Patient::getId)
                .toList();
    }

    private List<Patient> loadPatientsWithRelatedData(List<Long> patientIds) {
        return patientRepository.findPatientsByIdsWithVisitsAndDoctors(patientIds);
    }

    private List<PatientResponse> buildPatientResponses(
            List<Patient> patients,
            Map<Long, Integer> doctorPatientCounts,
            List<Long> doctorIds) {

        return patients.stream()
                .map(patient -> buildPatientResponse(patient, doctorPatientCounts, doctorIds))
                .toList();
    }

    private PatientResponse buildPatientResponse(
            Patient patient,
            Map<Long, Integer> doctorPatientCounts,
            List<Long> doctorIds) {

        List<VisitResponse> visitResponses = patient.getVisits().stream()
                .filter(createDoctorFilter(doctorIds))
                .map(visit -> createVisitResponse(visit, doctorPatientCounts))
                .toList();

        return PatientResponse.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .lastVisits(visitResponses)
                .build();
    }

    private Predicate<Visit> createDoctorFilter(List<Long> doctorIds) {
        return visit -> doctorIds == null ||
                        doctorIds.isEmpty() ||
                        doctorIds.contains(visit.getDoctor().getId());
    }

    private VisitResponse createVisitResponse(Visit visit, Map<Long, Integer> doctorPatientCounts) {
        VisitResponse response = MapperUtil.toVisitResponse(visit);
        Integer totalPatients = doctorPatientCounts.getOrDefault(visit.getDoctor().getId(), 0);
        return MapperUtil.updateDoctorTotalPatients(response, totalPatients);
    }
}
