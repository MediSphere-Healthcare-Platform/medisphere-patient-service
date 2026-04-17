package com.medisphere.patient.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllDoctorsDTO {
    private String firstName;
    private String lastName;
    private String specialty;
    private String profilePic;
    private String drContactNo;
    private String status;
}
