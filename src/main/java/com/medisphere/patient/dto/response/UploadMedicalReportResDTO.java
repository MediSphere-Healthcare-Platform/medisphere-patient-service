package com.medisphere.patient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadMedicalReportResDTO {
    private String reportId;
    private String reportName;
    private String fileUrl;
    private Instant uploadedAt;
}
