package com.medisphere.patient.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllPatientsForAdminDTO {

    private String patientId;
    private String msUserId;
    private String firstName;
    private String lastName;
    private String gender;
    private String profileImageUrl;
    private Instant createdAt;
    private Instant updatedAt;

}
