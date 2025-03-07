package com.healthcare.visittracker.repository;

import com.healthcare.visittracker.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE " +
           "(:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Patient> findAllWithFilters(@Param("search") String search, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Patient p JOIN p.visits v WHERE v.doctor.id IN :doctorIds " +
           "AND (:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Patient> findAllByDoctorIdsAndSearch(
            @Param("doctorIds") List<Long> doctorIds,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT COUNT(DISTINCT p) FROM Patient p JOIN p.visits v WHERE v.doctor.id IN :doctorIds " +
           "AND (:search IS NULL OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Long countByDoctorIdsAndSearch(
            @Param("doctorIds") List<Long> doctorIds,
            @Param("search") String search);
}
