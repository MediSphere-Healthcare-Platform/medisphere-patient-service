package com.medisphere.patient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePatientDetailsResDTO {
    private String patientId;
    private String msUserId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String bloodGroup;
    private String allergies;
    private String chronicConditions;
    private String profileImageUrl;
    private Instant updatedAt;
    private String status;
}
