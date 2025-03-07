package com.healthcare.visittracker.controller;

import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public PatientListResponse getAllPatients(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String doctorIds) {
        System.out.println("We are here");
        List<Long> doctorIdList = null;

        if (doctorIds != null && !doctorIds.isEmpty()) {
            doctorIdList = Arrays.stream(doctorIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .toList();
        }
        return patientService.getAllPatients(page, size, search, doctorIdList);
    }
}
