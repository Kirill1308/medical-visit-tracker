package com.healthcare.visittracker.service.impl;

import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
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

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientListResponse getAllPatients(Integer page, Integer size, String search, List<Long> doctorIds) {
        Pageable pageable = PaginationUtil.createPageRequest(page, size);
        boolean doctorIdsEmpty = doctorIds == null || doctorIds.isEmpty();

        Page<Object[]> patientsPage = patientRepository.findPatientsWithLastVisit(
                search,
                doctorIds,
                doctorIdsEmpty,
                pageable
        );

        List<PatientResponse> patientResponses = MapperUtil.toPatientResponses(patientsPage.getContent());

        return PatientListResponse.builder()
                .data(patientResponses)
                .count(patientsPage.getTotalElements())
                .build();
    }
}
