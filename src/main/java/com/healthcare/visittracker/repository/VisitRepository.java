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

    List<Visit> findByPatientIdIn(List<Long> patientIds);
}
