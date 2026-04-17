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
public class DeleteMedicalReportByIdReqDTO {
    @NotBlank(message = "Report ID is required")
    private String reportId;
}
