package com.medisphere.patient.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAllReportsByPatientIdReqDTO {
    @NotBlank(message = "Patient ID is required")
    private String patientId;
}
