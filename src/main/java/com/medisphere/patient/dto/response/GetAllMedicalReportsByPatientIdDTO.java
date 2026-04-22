package com.medisphere.patient.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllMedicalReportsByPatientIdDTO {

    private String reportId;
    private String patient;
    private String reportName;
    private String reportType;
    private String fileUrl;
    private String fileType;
    private String description;
    private String patientName;
    private Instant uploadedAt;

}
