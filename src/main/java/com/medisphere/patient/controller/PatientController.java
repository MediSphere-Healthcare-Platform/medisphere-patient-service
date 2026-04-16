package com.medisphere.patient.controller;

import com.medisphere.patient.client.AppointmentClient;
import com.medisphere.patient.client.DoctorClient;
import com.medisphere.patient.dto.request.*;
import com.medisphere.patient.dto.response.GetAllMedicalReportsByPatientIdDTO;
import com.medisphere.patient.dto.response.GetPatientByIdDTO;
import com.medisphere.patient.dto.response.UploadMedicalReportResDTO;
import com.medisphere.patient.service.MedicalReportService;
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
    private final MedicalReportService medicalReportService;

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
    public ResponseEntity<Object> bookAppointment(@Validated @RequestBody BookAppointmentRequestDTO bookAppointmentRequestDTO) {
        return new ResponseEntity<>(
                appointmentClient.bookAppointment(bookAppointmentRequestDTO),
                HttpStatus.OK
        );
    }

    @PostMapping(value = Endpoint.UPDATE_APPOINTMENT)
    public ResponseEntity<Object> updateAppointment(@Valid @RequestBody AppointmentUpdateRequestDTO appointmentUpdateRequestDTO) {
        return new ResponseEntity<>(
                appointmentClient.updateAppointment(appointmentUpdateRequestDTO),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = Endpoint.DELETE_PATIENT)
    public ResponseEntity<StandardResponse> deletePatient(@Valid @PathVariable("pid") String pid) {
        DeletePatientDTO deletePatientDTO = new DeletePatientDTO();
        deletePatientDTO.setPatientId(pid);
        return new ResponseEntity<>(
                new StandardResponse(200, "Patient deleted successfully", patientService.deletePatient(deletePatientDTO)),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = Endpoint.DELETE_APPOINTMENT)
    public ResponseEntity<Object> deleteAppointment(@PathVariable("appointmentReferenceId") String appointmentReferenceId) {
        return new ResponseEntity<>(
                appointmentClient.cancelAppointment(appointmentReferenceId),
                HttpStatus.OK
        );
    }

    @GetMapping(value = Endpoint.GET_PATIENT_REPORTS_BY_ID)
    public ResponseEntity<StandardResponse> getPatientReportsByPatientId(@Valid @PathVariable("pid") String pid) {
        GetPatientByIdReqDTO getPatientByIdReqDTO = new GetPatientByIdReqDTO();
        getPatientByIdReqDTO.setPatientId(pid);
        return new ResponseEntity<>(
                new StandardResponse(200, "Patient Reports Successfully fetched..", medicalReportService.getAllMedicalReportByPatientId(getPatientByIdReqDTO)),
                HttpStatus.OK
        );
    }

    @PostMapping(value = Endpoint.UPLOAD_MEDICAL_REPORT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StandardResponse> uploadMedicalReport(
            @Valid @RequestPart("reportData") UploadMedicalReportReqDTO dto,
            @RequestPart("file") MultipartFile file) {
        return new ResponseEntity<>(
                new StandardResponse(201, "Report Uploaded Successfully", medicalReportService.uploadMedicalReport(dto, file)),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(value = Endpoint.DELETE_MEDICAL_REPORT_BY_ID)
    public ResponseEntity<StandardResponse> deleteMedicalReportById(@PathVariable("reportId") String reportId) {
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", medicalReportService.deleteMedicalReportById(new DeleteMedicalReportByIdReqDTO(reportId))),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = Endpoint.DELETE_ALL_REPORTS_BY_PATIENT_ID)
    public ResponseEntity<StandardResponse> deleteAllMedicalReportsByPatientId(@PathVariable("pid") String pid) {
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", medicalReportService.deleteAllMedicalReportsByPatientId(new DeleteAllReportsByPatientIdReqDTO(pid))),
                HttpStatus.OK
        );
    }

    @PutMapping(value = Endpoint.UPDATE_PATIENT_DETAILS)
    public ResponseEntity<StandardResponse> updatePatientDetails(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdatePatientDetailsReqDTO updatePatientDetailsReqDTO) {
        return new ResponseEntity<>(
                new StandardResponse(200, "Patient Details Updated Successfully", patientService.updatePatient(id, updatePatientDetailsReqDTO)),
                HttpStatus.OK
        );
    }

    @GetMapping(value = Endpoint.GET_ALL_APPOINTMENTS_BY_PATIENT_ID)
    public ResponseEntity<Object> getAllAppointmentsByPatientId(@PathVariable("patientId") String patientId) {
        return new ResponseEntity<>(
                appointmentClient.getAllAppointmentsByPatientId(patientId),
                HttpStatus.OK
        );
    }

    @GetMapping(value = Endpoint.GET_MEDICAL_REPORTS_BY_DOCTOR_ID)
    public ResponseEntity<StandardResponse> getMedicalReportsByDoctorId(@PathVariable("doctorId") String doctorId) {
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", medicalReportService.getAllMedicalReportByDoctorId(doctorId)),
                HttpStatus.OK
        );
    }

    // Internal endpoint for service-to-service communication (e.g., from Auth Service)
    @PostMapping(value = "/createPatient/internal")
    public ResponseEntity<StandardResponse> createPatientInternal(
            @Valid @RequestBody CreatePatientDTO createPatientDTO
    ) {
        String patientId = patientService.createPatient(createPatientDTO, null);
        return new ResponseEntity<>(
                new StandardResponse(201, "Patient created successfully", patientId),
                HttpStatus.CREATED
        );
    }
}
