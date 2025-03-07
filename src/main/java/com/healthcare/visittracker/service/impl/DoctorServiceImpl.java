package com.healthcare.visittracker.service.impl;

import com.healthcare.visittracker.entity.Doctor;

import com.healthcare.visittracker.exception.custom.ResourceNotFoundException;
import com.healthcare.visittracker.repository.DoctorRepository;
import com.healthcare.visittracker.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public Integer countTotalPatients(Long doctorId) {
        return doctorRepository.countTotalPatients(doctorId);
    }
}
