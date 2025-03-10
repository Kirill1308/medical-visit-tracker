package com.healthcare.visittracker.repository;

import com.healthcare.visittracker.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query("SELECT v FROM Visit v " +
           "WHERE v.doctor.id = :doctorId " +
           "AND ((v.startDateTime <= :end " +
           "AND v.endDateTime >= :start))")
    List<Visit> findOverlappingVisits(
            @Param("doctorId") Long doctorId,
            @Param("start") ZonedDateTime start,
            @Param("end") ZonedDateTime end);

    @Query(value = "SELECT v.* FROM visits v " +
                   "INNER JOIN ( " +
                   "    SELECT patient_id, doctor_id, MAX(start_date_time) as max_start_time " +
                   "    FROM visits " +
                   "    GROUP BY patient_id, doctor_id " +
                   ") as latest ON v.patient_id = latest.patient_id " +
                   "AND v.doctor_id = latest.doctor_id " +
                   "AND v.start_date_time = latest.max_start_time " +
                   "WHERE v.patient_id = :patientId", nativeQuery = true)
    List<Visit> findLatestVisitsForPatient(@Param("patientId") Long patientId);

    @Query(value = "SELECT v.* FROM visits v " +
                   "INNER JOIN ( " +
                   "    SELECT patient_id, doctor_id, MAX(start_date_time) as max_start_time " +
                   "    FROM visits " +
                   "    WHERE doctor_id IN :doctorIds " +
                   "    GROUP BY patient_id, doctor_id " +
                   ") as latest ON v.patient_id = latest.patient_id " +
                   "AND v.doctor_id = latest.doctor_id " +
                   "AND v.start_date_time = latest.max_start_time " +
                   "WHERE v.patient_id = :patientId", nativeQuery = true)
    List<Visit> findLatestVisitsForPatientByDoctorIds(
            @Param("patientId") Long patientId,
            @Param("doctorIds") List<Long> doctorIds);
}
