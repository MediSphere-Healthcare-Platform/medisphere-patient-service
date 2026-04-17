package com.medisphere.patient.client;

import com.medisphere.patient.util.StandardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "medisphere-doctor-service", path = "/doctor/api/v1")
public interface DoctorClient {

    @GetMapping(value = "/getAllDoctors")
    StandardResponse getAllDoctors();
}
