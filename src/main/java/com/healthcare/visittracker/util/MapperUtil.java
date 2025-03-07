package com.healthcare.visittracker.util;

import com.healthcare.visittracker.dto.response.DoctorResponse;
import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
import com.healthcare.visittracker.dto.response.VisitResponse;
import com.healthcare.visittracker.entity.Doctor;
import com.healthcare.visittracker.entity.Visit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtil {

    public static DoctorResponse toDoctorResponse(Doctor doctor, Integer totalPatients) {
        if (doctor == null) {
            return null;
        }

        return DoctorResponse.builder()
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .totalPatients(totalPatients)
                .build();
    }

    public static VisitResponse toVisitResponse(Visit visit) {
        if (visit == null) {
            return null;
        }

        return VisitResponse.builder()
                .start(DateTimeUtil.formatDateTime(visit.getStartDateTime()))
                .end(DateTimeUtil.formatDateTime(visit.getEndDateTime()))
                .doctor(toDoctorResponse(visit.getDoctor(), null))
                .build();
    }

    public static PatientListResponse toPatientListResponse(List<PatientResponse> patients, long totalCount) {
        return PatientListResponse.builder()
                .data(patients != null ? patients : Collections.emptyList())
                .count((int) totalCount)
                .build();
    }

    public static VisitResponse updateDoctorTotalPatients(VisitResponse visitResponse, Integer totalPatients) {
        if (visitResponse == null || visitResponse.getDoctor() == null) {
            return visitResponse;
        }

        DoctorResponse updatedDoctor = DoctorResponse.builder()
                .firstName(visitResponse.getDoctor().getFirstName())
                .lastName(visitResponse.getDoctor().getLastName())
                .totalPatients(totalPatients)
                .build();

        return VisitResponse.builder()
                .start(visitResponse.getStart())
                .end(visitResponse.getEnd())
                .doctor(updatedDoctor)
                .build();
    }
}
