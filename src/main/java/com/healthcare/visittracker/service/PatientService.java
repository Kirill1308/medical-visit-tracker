package com.healthcare.visittracker.service;

import com.healthcare.visittracker.dto.response.PatientListResponse;

import java.util.List;

public interface PatientService {
    PatientListResponse getAllPatients(Integer page, Integer size, String search, List<Long> doctorIds);
}
