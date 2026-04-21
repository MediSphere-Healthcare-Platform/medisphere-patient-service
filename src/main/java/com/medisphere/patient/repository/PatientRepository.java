package com.medisphere.patient.repository;

import com.medisphere.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<PatientEntity, Integer> {

    @Query(value = "SELECT * FROM medisphere_patient WHERE patient_id = :patientId", nativeQuery = true)
    PatientEntity findByPatientId(@Param("patientId") String patientId);

    @Query(value = "SELECT MAX(CAST(SUBSTRING(ms_user_id, 3) AS BIGINT)) FROM medisphere_patient", nativeQuery = true)
    Long findMaxMsUserId();

    @Query(value = "SELECT patient_id FROM medisphere_patient ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastPatientId();

    @Query(value = "SELECT * FROM medisphere_patient WHERE ms_user_id = :msUserId", nativeQuery = true)
    PatientEntity checkPatientByMsUserId(@Param("msUserId") String msUserId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM medisphere_patient WHERE patient_id = :patientId", nativeQuery = true)
    void deletePatientByPatientId(@Param("patientId") String patientId);
}
