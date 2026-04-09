package com.medisphere.patient.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateRequestDTO {

    @NotNull(message = "Appointment reference ID cannot be null")
    @NotEmpty(message = "Appointment reference ID cannot be empty")
    private String appointmentReferenceId;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    @NotEmpty(message = "Appointment reason cannot be empty")
    private String reason;

    @NotEmpty(message = "Appointment Doctor ID cannot be empty")
    private String doctorId;

}
