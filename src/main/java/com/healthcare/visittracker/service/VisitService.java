package com.healthcare.visittracker.service;

import com.healthcare.visittracker.dto.request.VisitRequest;
import com.healthcare.visittracker.entity.Visit;

import java.util.List;

public interface VisitService {
    void createVisit(VisitRequest request);

    List<Visit> findLatestVisitsForPatient(Long patientId);

    List<Visit> findLatestVisitsForPatientByDoctorIds(Long patientId, List<Long> doctorIds);
}
