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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
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

        Instant startInstant = DateTimeUtil.parseToInstant(request.getStart(), ZoneId.of("UTC"));
        Instant endInstant = DateTimeUtil.parseToInstant(request.getEnd(), ZoneId.of("UTC"));

        if (endInstant.isBefore(startInstant) || endInstant.equals(startInstant)) {
            throw new InvalidTimezoneException("End time must be after start time");
        }

        List<Visit> overlappingVisits = visitRepository.findOverlappingVisits(
                doctor.getId(), startInstant, endInstant);

        if (!overlappingVisits.isEmpty()) {
            throw new VisitConflictException("Doctor already has a visit scheduled during this time");
        }

        Visit visit = Visit.builder()
                .startDateTime(startInstant)
                .endDateTime(endInstant)
                .doctor(doctor)
                .patient(patient)
                .build();

        visitRepository.save(visit);
    }
}
