package com.healthcare.visittracker.util;

import com.healthcare.visittracker.dto.projection.PatientProjection;
import com.healthcare.visittracker.dto.response.DoctorResponse;
import com.healthcare.visittracker.dto.response.PatientListResponse;
import com.healthcare.visittracker.dto.response.PatientResponse;
import com.healthcare.visittracker.dto.response.VisitResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PatientMapper {
    public PatientListResponse toPatientListResponse(Page<PatientProjection> projections) {
        Map<Long, PatientResponse> patientResponseMap = new LinkedHashMap<>();

        for (PatientProjection projection : projections) {
            Long patientId = projection.getPatientId();

            PatientResponse patientResponse = patientResponseMap.computeIfAbsent(patientId, id ->
                    PatientResponse.builder()
                            .firstName(projection.getPatientFirstName())
                            .lastName(projection.getPatientLastName())
                            .lastVisits(new ArrayList<>())
                            .build()
            );

            if (projection.getVisitId() != null) {
                DoctorResponse doctorResponse = toDoctorResponse(projection);
                VisitResponse visitResponse = toVisitResponse(projection, doctorResponse);
                patientResponse.getLastVisits().add(visitResponse);
            }
        }

        List<PatientResponse> patientResponses = new ArrayList<>(patientResponseMap.values());

        return PatientListResponse.builder()
                .data(patientResponses)
                .count(projections.getTotalElements())
                .build();
    }

    private DoctorResponse toDoctorResponse(PatientProjection projection) {
        return DoctorResponse.builder()
                .firstName(projection.getDoctorFirstName())
                .lastName(projection.getDoctorLastName())
                .totalPatients(projection.getDoctorTotalPatients())
                .build();
    }

    private VisitResponse toVisitResponse(PatientProjection projection, DoctorResponse doctorResponse) {
        ZoneId doctorZoneId = ZoneId.of(projection.getDoctorTimezone());

        Instant startInstant = projection.getVisitStartDateTime();
        Instant endInstant = projection.getVisitEndDateTime();

        String formattedStart = DateTimeUtil.formatWithOffset(startInstant, doctorZoneId);
        String formattedEnd = DateTimeUtil.formatWithOffset(endInstant, doctorZoneId);

        return VisitResponse.builder()
                .start(formattedStart)
                .end(formattedEnd)
                .doctor(doctorResponse)
                .build();
    }
}
