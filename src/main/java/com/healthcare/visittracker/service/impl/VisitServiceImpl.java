package com.healthcare.visittracker.service.impl;

import com.healthcare.visittracker.dto.request.VisitRequest;
import com.healthcare.visittracker.entity.Doctor;
import com.healthcare.visittracker.entity.Patient;
import com.healthcare.visittracker.entity.Visit;
import com.healthcare.visittracker.exception.custom.InvalidTimezoneException;
import com.healthcare.visittracker.exception.custom.ResourceNotFoundException;
import com.healthcare.visittracker.exception.custom.VisitConflictException;
import com.healthcare.visittracker.repository.DoctorRepository;
import com.healthcare.visittracker.repository.PatientRepository;
import com.healthcare.visittracker.repository.VisitRepository;
import com.healthcare.visittracker.service.VisitService;
import com.healthcare.visittracker.util.DateTimeUtil;
import com.healthcare.visittracker.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public void createVisit(VisitRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));

        ZonedDateTime startDateTime = DateTimeUtil.parseDateTime(request.getStart());
        ZonedDateTime endDateTime = DateTimeUtil.parseDateTime(request.getEnd());

        if (!ValidationUtil.validateTimeRange(startDateTime, endDateTime)) {
            throw new InvalidTimezoneException("End time must be after start time");
        }

        List<Visit> overlappingVisits = visitRepository.findOverlappingVisits(doctor.getId(), startDateTime, endDateTime);

        if (!overlappingVisits.isEmpty()) {
            throw new VisitConflictException("Doctor already has a visit scheduled during this time");
        }

        Visit visit = Visit.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .doctor(doctor)
                .patient(patient)
                .build();

        visitRepository.save(visit);
    }
}
