package com.medisphere.patient.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentCancelRequestDTO {

    @NotNull(message = "Appointment reference ID cannot be null")
    @NotEmpty(message = "Appointment reference ID cannot be empty")
    private String appointmentReferenceId;

}
