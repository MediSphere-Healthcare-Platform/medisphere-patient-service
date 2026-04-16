package com.medisphere.patient.repository;

import com.medisphere.patient.entity.MedisphereMedicalReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MedicalReportRepository extends JpaRepository<MedisphereMedicalReportEntity, Integer> {

    @Query(value = "SELECT * FROM medisphere_medical_report WHERE patient_id = :patientId", nativeQuery = true)
    List<MedisphereMedicalReportEntity> findAllByPatient(@Param("patientId") String patientId);

    @Query(value = "SELECT * FROM medisphere_medical_report WHERE doctor_id = :doctorId", nativeQuery = true)
    List<MedisphereMedicalReportEntity> findAllByDoctorId(@Param("doctorId") String doctorId);

    @Query(value = "SELECT report_id FROM medisphere_medical_report ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastReportId();

    @Query(value = "SELECT * FROM medisphere_medical_report WHERE report_id = :reportId", nativeQuery = true)
    MedisphereMedicalReportEntity findByReportId(@Param("reportId") String reportId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medisphere_medical_report WHERE report_id = :reportId", nativeQuery = true)
    void deleteByReportId(@Param("reportId") String reportId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medisphere_medical_report WHERE patient_id = :patientId", nativeQuery = true)
    void deleteAllByPatientId(@Param("patientId") String patientId);
}
