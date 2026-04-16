package com.medisphere.patient.repository;

import com.medisphere.patient.entity.MedisphereMedicalReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicalReportRepository extends JpaRepository<MedisphereMedicalReportEntity, Integer> {

    @Query(value = "SELECT * FROM medisphere_medical_report WHERE patient_id = :patientId", nativeQuery = true)
    List<MedisphereMedicalReportEntity> findAllByPatient(@Param("patientId") String patientId);
}
