package com.healthcare.visittracker.repository;

import com.healthcare.visittracker.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT COUNT(DISTINCT v.patient.id) FROM Visit v WHERE v.doctor.id = :doctorId")
    Integer countTotalPatients(@Param("doctorId") Long doctorId);

    Optional<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
}
