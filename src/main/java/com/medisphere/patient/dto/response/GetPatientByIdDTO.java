package com.medisphere.patient.dto.response;

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
public class GetPatientByIdDTO {

    private String patientId;
    private String msUserId;
    private String firstName;
    private String lastName;
    private String gender;
    private String chronicConditions;
    private String allergies;
    private String bloodGroup;
    private String address;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String profileImageUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private String status;
}
