package com.medisphere.patient.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePatientDTO {
    private String patientId;

    @NotNull(message = "msUserId can't be Null")
    @NotEmpty(message = "msUserId can't be Empty")
    private String msUserId;

    @NotNull(message = "firstName can't be Null")
    @NotEmpty(message = "firstName can't be Empty")
    private String firstName;

    @NotNull(message = "lastName can't be Null")
    @NotEmpty(message = "lastName can't be Empty")
    private String lastName;

    private LocalDate dateOfBirth;

    private String gender;

    @NotNull(message = "Phone Number can't be Null")
    @NotEmpty(message = "Phone Number can't be Empty")
    private String phoneNumber;

    private String address;

    private String bloodGroup;
    private String allergies;
    private String chronicConditions;
    private String profileImageUrl;
}
