package com.healthcare.visittracker.dto.projection;

public interface PatientProjection {
    Long getPatientId();
    String getPatientFirstName();
    String getPatientLastName();

    Long getVisitId();
    String getVisitStartDateTime();
    String getVisitEndDateTime();

    Long getDoctorId();
    String getDoctorFirstName();
    String getDoctorLastName();
    Integer getDoctorTotalPatients();
}
