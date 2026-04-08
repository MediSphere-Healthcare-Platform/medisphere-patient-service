package com.medisphere.patient.controller;

import com.medisphere.patient.client.DoctorClient;
import com.medisphere.patient.dto.request.GetPatientByIdReqDTO;
import com.medisphere.patient.dto.response.GetPatientByIdDTO;
import com.medisphere.patient.service.PatientService;
import com.medisphere.patient.util.Endpoint;
import com.medisphere.patient.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/patient/api/v1")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final DoctorClient doctorClient;

    @GetMapping(value = Endpoint.GET_ALL_DOCTORS_FOR_PATIENT)
    public ResponseEntity<StandardResponse> getAllDoctorsForPatient() {
        return new ResponseEntity<>(
                doctorClient.getAllDoctors(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = Endpoint.GET_ALL_PATIENT_FOR_ADMIN)
    public ResponseEntity<StandardResponse> getAllPatientForAdmin() {
        return new ResponseEntity<>(
                new StandardResponse(200, "Patient retrieved successfully", patientService.getAllPatientsForAdmin()),
                HttpStatus.OK
        );
    }

    @GetMapping(value = Endpoint.GET_PATIENT_BY_ID)
    public ResponseEntity<StandardResponse> getPatientById(@PathVariable("id") String id) {
        GetPatientByIdReqDTO getPatientByIdReqDTO = new GetPatientByIdReqDTO();
        getPatientByIdReqDTO.setPatientId(id);
        return new ResponseEntity<>(
                new StandardResponse(200, "Patient data Successfully retrieved", patientService.getPatientById(getPatientByIdReqDTO)),
                HttpStatus.OK
        );
    }
}
