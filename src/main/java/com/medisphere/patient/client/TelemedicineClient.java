package com.medisphere.patient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "medisphere-telemedicine-service", path = "/api")
public interface TelemedicineClient {

    @GetMapping("/sessions/patient/{patientId}")
    Object getSessionsByPatientId(@PathVariable("patientId") String patientId);

    @GetMapping("/sessions/{sessionId}")
    Object getSessionById(@PathVariable("sessionId") String sessionId, @RequestParam("userId") String userId, @RequestParam("role") String role);

    @GetMapping("/prescriptions/patient/{patientId}")
    Object getPrescriptionsByPatientId(@PathVariable("patientId") String patientId);

    @DeleteMapping("/prescriptions/{prescriptionId}")
    Object deletePrescription(@PathVariable("prescriptionId") String prescriptionId, @RequestParam("doctorUserId") String doctorUserId);
}
