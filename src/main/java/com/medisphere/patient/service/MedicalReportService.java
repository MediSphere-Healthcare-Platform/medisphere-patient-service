package com.medisphere.patient.service;

import com.medisphere.patient.dto.request.GetPatientByIdReqDTO;
import com.medisphere.patient.dto.response.GetAllMedicalReportsByPatientIdDTO;
import com.medisphere.patient.entity.MedisphereMedicalReportEntity;
import com.medisphere.patient.entity.PatientEntity;
import com.medisphere.patient.exception.EntryNotFoundException;
import com.medisphere.patient.repository.MedicalReportRepository;
import com.medisphere.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalReportService {

    private final MedicalReportRepository medicalReportRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public List<GetAllMedicalReportsByPatientIdDTO> getAllMedicalReportByPatientId(GetPatientByIdReqDTO getPatientByIdReqDTO){

        try{
            PatientEntity patient = patientRepository.findByPatientId(getPatientByIdReqDTO.getPatientId());
            if (patient == null) {
                throw new EntryNotFoundException("According to given patient ID patient doesn't exists");
            }

            List<MedisphereMedicalReportEntity> patientReports = medicalReportRepository.findAllByPatient(getPatientByIdReqDTO.getPatientId());
            return modelMapper.map(patientReports, new TypeToken<List<GetAllMedicalReportsByPatientIdDTO>>(){}.getType());

        }catch(EntryNotFoundException e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch reports: " + e.getMessage());
        }
    }
}
