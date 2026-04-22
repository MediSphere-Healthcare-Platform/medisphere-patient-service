package com.medisphere.patient.client;

import com.medisphere.patient.dto.request.AppointmentUpdateRequestDTO;
import com.medisphere.patient.dto.request.BookAppointmentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "medisphere-appointment-service", path = "/api/v1")
public interface AppointmentClient {
    @PostMapping(value = "/appointments/bookAppointment")
    Object bookAppointment(@RequestBody BookAppointmentRequestDTO bookAppointmentRequestDTO);

    @PutMapping(value = "/appointments/updateAppointment")
    Object updateAppointment(@RequestBody AppointmentUpdateRequestDTO appointmentUpdateRequestDTO);

    @DeleteMapping(value = "/appointments/cancel/{appointmentReferenceId}")
    Object cancelAppointment(@PathVariable("appointmentReferenceId") String appointmentReferenceId);

    @GetMapping(value = "/appointments/allAppointmentsByPatientId/{patientId}")
    Object getAllAppointmentsByPatientId(@PathVariable("patientId") String patientId);
}
