package com.healthcare.visittracker.repository;

import com.healthcare.visittracker.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByFirstNameAndLastName(String firstName, String lastName);
}
