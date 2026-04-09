package com.medisphere.patient.controller;

import com.medisphere.patient.client.AppointmentClient;
import com.medisphere.patient.client.DoctorClient;
import com.medisphere.patient.dto.request.BookAppointmentRequestDTO;
import com.medisphere.patient.dto.request.CreatePatientDTO;
import com.medisphere.patient.dto.request.GetPatientByIdReqDTO;
import com.medisphere.patient.dto.response.GetPatientByIdDTO;
import com.medisphere.patient.service.PatientService;
import com.medisphere.patient.util.Endpoint;
import com.medisphere.patient.util.StandardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/patient/api/v1")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final DoctorClient doctorClient;
    private final AppointmentClient appointmentClient;

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

    @PostMapping(
            value = Endpoint.CREATE_PATIENT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StandardResponse> createPatient(
            @Valid @RequestPart("patient") CreatePatientDTO createPatientDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        String patientId = patientService.createPatient(createPatientDTO, profileImage);
        return new ResponseEntity<>(
                new StandardResponse(201, "Patient created successfully", patientId),
                HttpStatus.CREATED
        );
    }

    @PostMapping(value = Endpoint.BOOK_AN_APPOINTMENT)
    public ResponseEntity<Object> bookAppointment(@Valid @RequestBody BookAppointmentRequestDTO bookAppointmentRequestDTO) {
        return new ResponseEntity<>(
                appointmentClient.bookAppointment(bookAppointmentRequestDTO),
                HttpStatus.OK
        );
    }
}
