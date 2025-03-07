package com.healthcare.visittracker.service.impl;

import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
import com.healthcare.visittracker.dto.response.VisitResponse;
import com.healthcare.visittracker.entity.Patient;
import com.healthcare.visittracker.entity.Visit;
import com.healthcare.visittracker.repository.PatientRepository;
import com.healthcare.visittracker.service.DoctorService;
import com.healthcare.visittracker.service.PatientService;
import com.healthcare.visittracker.service.VisitService;
import com.healthcare.visittracker.util.MapperUtil;
import com.healthcare.visittracker.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final VisitService visitService;
    private final DoctorService doctorService;

    @Override
    public PatientListResponse getAllPatients(Integer page, Integer size, String search, List<Long> doctorIds) {
        System.out.println("We are here");
        Pageable pageable = PaginationUtil.createPageRequest(page, size);

        Page<Patient> patientsPage;
        Long totalCount;

        if (doctorIds == null || doctorIds.isEmpty()) {
            patientsPage = patientRepository.findAllWithFilters(search, pageable);
            totalCount = patientsPage.getTotalElements();
        } else {
            patientsPage = patientRepository.findAllByDoctorIdsAndSearch(doctorIds, search, pageable);
            totalCount = patientRepository.countByDoctorIdsAndSearch(doctorIds, search);
        }

        List<PatientResponse> patientResponses = patientsPage.getContent().stream()
                .map(patient -> {
                    List<Visit> latestVisits = getLatestVisits(patient.getId(), doctorIds);

                    List<VisitResponse> visitResponses = latestVisits.stream()
                            .map(visit -> {
                                VisitResponse visitResponse = MapperUtil.toVisitResponse(visit);
                                Integer totalPatients = doctorService.countTotalPatients(visit.getDoctor().getId());
                                return MapperUtil.updateDoctorTotalPatients(visitResponse, totalPatients);
                            })
                            .toList();

                    return PatientResponse.builder()
                            .firstName(patient.getFirstName())
                            .lastName(patient.getLastName())
                            .lastVisits(visitResponses)
                            .build();
                })
                .toList();

        return MapperUtil.toPatientListResponse(patientResponses, totalCount);
    }

    private List<Visit> getLatestVisits(Long patientId, List<Long> doctorIds) {
        if (doctorIds == null || doctorIds.isEmpty()) {
            return visitService.findLatestVisitsForPatient(patientId);
        } else {
            return visitService.findLatestVisitsForPatientByDoctorIds(patientId, doctorIds);
        }
    }
}
