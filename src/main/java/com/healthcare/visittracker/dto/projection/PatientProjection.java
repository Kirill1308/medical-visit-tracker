package com.healthcare.visittracker.dto.projection;

import java.time.Instant;

public interface PatientProjection {
    Long getPatientId();
    String getPatientFirstName();
    String getPatientLastName();

    Long getVisitId();
    Instant getVisitStartDateTime();
    Instant getVisitEndDateTime();

    Long getDoctorId();
    String getDoctorFirstName();
    String getDoctorLastName();
    String getDoctorTimezone();
    Integer getDoctorTotalPatients();
}
