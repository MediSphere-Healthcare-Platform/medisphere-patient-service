package com.medisphere.patient.client;

import com.medisphere.patient.dto.request.BookAppointmentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "medisphere-appointment-service", path = "/api/v1")
public interface AppointmentClient {
    @PostMapping(value = "appointments/bookAppointment")
    Object bookAppointment(@RequestBody BookAppointmentRequestDTO bookAppointmentRequestDTO);
}
