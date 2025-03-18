package com.healthcare.visittracker.repository;

import com.healthcare.visittracker.dto.projection.PatientProjection;
import com.healthcare.visittracker.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    @Query(value = "WITH doctor_totals AS (" +
                   "  SELECT d.id, COUNT(DISTINCT v.patient_id) AS total_patients" +
                   "  FROM doctors d" +
                   "  JOIN visits v ON d.id = v.doctor_id" +
                   "  GROUP BY d.id" +
                   ")" +
                   "SELECT " +
                   "p.id AS patientId, " +
                   "p.first_name AS patientFirstName, " +
                   "p.last_name AS patientLastName, " +
                   "v.id AS visitId, " +
                   "v.start_date_time AS visitStartDateTime, " +
                   "v.end_date_time AS visitEndDateTime, " +
                   "d.id AS doctorId, " +
                   "d.first_name AS doctorFirstName, " +
                   "d.last_name AS doctorLastName, " +
                   "d.timezone AS doctorTimezone, " +
                   "COALESCE(dt.total_patients, 0) AS doctorTotalPatients " +
                   "FROM patients p " +
                   "LEFT JOIN visits v ON p.id = v.patient_id " +
                   "LEFT JOIN doctors d ON v.doctor_id = d.id " +
                   "LEFT JOIN doctor_totals dt ON d.id = dt.id " +
                   "WHERE (:search IS NULL OR " +
                   "LOWER(p.first_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                   "LOWER(p.last_name) LIKE LOWER(CONCAT('%', :search, '%')))" +
                   "ORDER BY p.id, v.start_date_time DESC",
            countQuery = "SELECT COUNT(DISTINCT p.id) " +
                         "FROM patients p " +
                         "WHERE (:search IS NULL OR " +
                         "LOWER(p.first_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                         "LOWER(p.last_name) LIKE LOWER(CONCAT('%', :search, '%')))",
            nativeQuery = true)
    Page<PatientProjection> findPatientsWithVisitsAndDoctors(
            @Param("search") String search,
            Pageable pageable);

    @Query(value = "WITH doctor_totals AS (" +
                   "  SELECT d.id, COUNT(DISTINCT v.patient_id) AS total_patients" +
                   "  FROM doctors d" +
                   "  JOIN visits v ON d.id = v.doctor_id" +
                   "  GROUP BY d.id" +
                   ")" +
                   "SELECT " +
                   "p.id AS patientId, " +
                   "p.first_name AS patientFirstName, " +
                   "p.last_name AS patientLastName, " +
                   "v.id AS visitId, " +
                   "v.start_date_time AS visitStartDateTime, " +
                   "v.end_date_time AS visitEndDateTime, " +
                   "d.id AS doctorId, " +
                   "d.first_name AS doctorFirstName, " +
                   "d.last_name AS doctorLastName, " +
                   "d.timezone AS doctorTimezone, " +
                   "COALESCE(dt.total_patients, 0) AS doctorTotalPatients " +
                   "FROM patients p " +
                   "JOIN visits v ON p.id = v.patient_id " +
                   "JOIN doctors d ON v.doctor_id = d.id " +
                   "LEFT JOIN doctor_totals dt ON d.id = dt.id " +
                   "WHERE (:search IS NULL OR " +
                   "LOWER(p.first_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                   "LOWER(p.last_name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                   "AND d.id IN (:doctorIds)" +
                   "ORDER BY p.id, v.start_date_time DESC",
            countQuery = "SELECT COUNT(DISTINCT p.id) " +
                         "FROM patients p " +
                         "JOIN visits v ON p.id = v.patient_id " +
                         "JOIN doctors d ON v.doctor_id = d.id " +
                         "WHERE (:search IS NULL OR " +
                         "LOWER(p.first_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                         "LOWER(p.last_name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                         "AND d.id IN (:doctorIds)",
            nativeQuery = true)
    Page<PatientProjection> findPatientsWithVisitsAndDoctorsByDoctorIds(
            @Param("search") String search,
            @Param("doctorIds") List<Long> doctorIds,
            Pageable pageable);
}
