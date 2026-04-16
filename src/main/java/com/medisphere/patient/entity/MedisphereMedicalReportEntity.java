package com.medisphere.patient.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "medisphere_medical_report")
public class MedisphereMedicalReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "report_id", nullable = false, length = 50)
    private String reportId;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private String patient;

    @Size(max = 100)
    @NotNull
    @Column(name = "report_name", nullable = false, length = 100)
    private String reportName;

    @Size(max = 50)
    @Column(name = "report_type", length = 50)
    private String reportType;

    @NotNull
    @Column(name = "file_url", nullable = false, length = Integer.MAX_VALUE)
    private String fileUrl;

    @Size(max = 20)
    @Column(name = "file_type", length = 20)
    private String fileType;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @Size(max = 50)
    @Column(name = "doctor_id", length = 50)
    private String doctorId;
}