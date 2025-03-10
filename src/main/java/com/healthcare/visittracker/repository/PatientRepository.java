package com.healthcare.visittracker.repository;

import com.healthcare.visittracker.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query(value = "SELECT DISTINCT p FROM Patient p " +
                   "WHERE (:search IS NULL OR " +
                   "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                   "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Patient p " +
                         "WHERE (:search IS NULL OR " +
                         "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                         "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Patient> findAllWithFilters(@Param("search") String search, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Patient p " +
           "LEFT JOIN FETCH p.visits v " +
           "LEFT JOIN FETCH v.doctor d " +
           "WHERE p.id IN :patientIds")
    List<Patient> findPatientsByIdsWithVisitsAndDoctors(@Param("patientIds") List<Long> patientIds);

    @Query(value = "SELECT DISTINCT p FROM Patient p " +
                   "JOIN p.visits v " +
                   "WHERE v.doctor.id IN :doctorIds " +
                   "AND (:search IS NULL OR " +
                   "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                   "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))",
            countQuery = "SELECT COUNT(DISTINCT p) FROM Patient p " +
                         "JOIN p.visits v " +
                         "WHERE v.doctor.id IN :doctorIds " +
                         "AND (:search IS NULL OR " +
                         "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                         "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Patient> findAllByDoctorIdsAndSearch(
            @Param("doctorIds") List<Long> doctorIds,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT v.doctor.id as doctorId, COUNT(DISTINCT v.patient.id) as patientCount " +
           "FROM Visit v GROUP BY v.doctor.id")
    List<Object[]> getDoctorPatientCountsList();

    default Map<Long, Integer> getDoctorPatientCounts() {
        List<Object[]> results = getDoctorPatientCountsList();
        return results.stream()
                .collect(java.util.stream.Collectors.toMap(
                        row -> (Long) row[0],  // doctorId
                        row -> ((Number) row[1]).intValue()  // patientCount
                ));
    }
}
