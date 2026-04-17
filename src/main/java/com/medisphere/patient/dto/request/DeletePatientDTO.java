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
public class DeletePatientDTO {

    @NotNull(message = "Patient ID can't be null")
    @NotEmpty(message = "Patient ID can't be empty")
    private String patientId;
}
