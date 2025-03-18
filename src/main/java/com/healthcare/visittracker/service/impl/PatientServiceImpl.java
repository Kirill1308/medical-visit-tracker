package com.healthcare.visittracker.service.impl;

import com.healthcare.visittracker.dto.projection.PatientProjection;
import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.repository.PatientRepository;
import com.healthcare.visittracker.service.PatientService;
import com.healthcare.visittracker.util.PaginationUtil;
import com.healthcare.visittracker.util.PatientMapper;
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
    private final PatientMapper patientMapper;

    @Override
    @Transactional(readOnly = true)
    public PatientListResponse getAllPatients(Integer page, Integer size, String search, List<Long> doctorIds) {
        Pageable pageable = PaginationUtil.createPageRequest(page, size);

        Page<PatientProjection> patientProjections;

        if (doctorIds == null || doctorIds.isEmpty()) {
            patientProjections = patientRepository.findPatientsWithVisitsAndDoctors(search, pageable);
        } else {
            patientProjections = patientRepository.findPatientsWithVisitsAndDoctorsByDoctorIds(search, doctorIds, pageable);
        }

        return patientMapper.toPatientListResponse(patientProjections);
    }
}
