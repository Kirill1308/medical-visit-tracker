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

    @Query(value = """
            SELECT
                p.id as patientId,
                p.firstName as patientFirstName,
                p.lastName as patientLastName,
                v.id as visitId,
                v.startDateTime as visitStart,
                v.endDateTime as visitEnd,
                d.id as doctorId,
                d.firstName as doctorFirstName,
                d.lastName as doctorLastName,
                (SELECT COUNT(DISTINCT v2.patient.id) FROM Visit v2 WHERE v2.doctor = d) as doctorTotalPatients
            FROM
                Patient p
            LEFT JOIN
                p.visits v
            LEFT JOIN
                v.doctor d
            WHERE
                (:search IS NULL OR
                p.firstName LIKE CONCAT('%', :search, '%') OR
                p.lastName LIKE CONCAT('%', :search, '%'))
                AND (
                    (:doctorIdsEmpty = true) OR
                    (d.id IN :doctorIds)
                )
            ORDER BY
                p.lastName ASC,
                p.firstName ASC,
                v.startDateTime DESC
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT p)
                    FROM Patient p
                    LEFT JOIN p.visits v
                    LEFT JOIN v.doctor d
                    WHERE
                        (:search IS NULL OR
                        p.firstName LIKE CONCAT('%', :search, '%') OR
                        p.lastName LIKE CONCAT('%', :search, '%'))
                        AND (
                            (:doctorIdsEmpty = true) OR
                            (d.id IN :doctorIds)
                        )
                    """)
    Page<Object[]> findPatientsWithLastVisit(
            @Param("search") String search,
            @Param("doctorIds") List<Long> doctorIds,
            @Param("doctorIdsEmpty") Boolean doctorIdsEmpty,
            Pageable pageable);
}
