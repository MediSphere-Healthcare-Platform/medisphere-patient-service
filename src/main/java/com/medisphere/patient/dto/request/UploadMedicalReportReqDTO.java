package com.medisphere.patient.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadMedicalReportReqDTO {

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Report Name is required")
    private String reportName;

    @NotBlank(message = "Report Type is required")
    private String reportType;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    private String description;
}
