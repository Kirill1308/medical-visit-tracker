package com.healthcare.visittracker.util;

import com.healthcare.visittracker.dto.response.DoctorResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
import com.healthcare.visittracker.dto.response.VisitResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtil {
    public static List<PatientResponse> toPatientResponses(List<Object[]> results) {
        Map<String, PatientResponse> patientMap = new HashMap<>();

        for (Object[] row : results) {
            // Patient data
            Long patientId = (Long) row[0];
            String patientFirstName = (String) row[1];
            String patientLastName = (String) row[2];
            String patientKey = patientId.toString();

            PatientResponse patientResponse = patientMap.computeIfAbsent(patientKey, k ->
                    PatientResponse.builder()
                            .firstName(patientFirstName)
                            .lastName(patientLastName)
                            .lastVisits(new ArrayList<>())
                            .build()
            );

            // Visit data
            Long visitId = (Long) row[3];
            if (visitId != null) {
                String visitStart = row[4] != null ? row[4].toString() : null;
                String visitEnd = row[5] != null ? row[5].toString() : null;

                // Doctor data
                Long doctorId = (Long) row[6];
                String doctorFirstName = (String) row[7];
                String doctorLastName = (String) row[8];
                Integer totalPatients = row[9] != null ? ((Number) row[9]).intValue() : 0;

                DoctorResponse doctorResponse = DoctorResponse.builder()
                        .firstName(doctorFirstName)
                        .lastName(doctorLastName)
                        .totalPatients(totalPatients)
                        .build();

                VisitResponse visitResponse = VisitResponse.builder()
                        .start(visitStart)
                        .end(visitEnd)
                        .doctor(doctorResponse)
                        .build();

                patientResponse.getLastVisits().add(visitResponse);
            }
        }
        return new ArrayList<>(patientMap.values());
    }
}
