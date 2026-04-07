package com.medisphere.patient.service;

import com.medisphere.patient.dto.request.GetPatientByIdReqDTO;
import com.medisphere.patient.dto.response.GetPatientByIdDTO;
import com.medisphere.patient.dto.response.GetAllPatientsForAdminDTO;
import com.medisphere.patient.entity.PatientEntity;
import com.medisphere.patient.exception.EntryNotFoundException;
import com.medisphere.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    public List<GetAllPatientsForAdminDTO> getAllPatientsForAdmin(){
        try{
            List<PatientEntity> patientEntity = patientRepository.findAll();
            return modelMapper.map(patientEntity, new TypeToken<List<GetAllPatientsForAdminDTO>>() {}.getType());
        }catch(Exception e){
            throw new RuntimeException("Failed to fetch Patients: " + e.getMessage());
        }
    }

    public GetPatientByIdDTO getPatientById(GetPatientByIdReqDTO getPatientByIdReqDTO){
        try{
            PatientEntity patient = patientRepository.findByPatientId(getPatientByIdReqDTO.getPatientId());

            if(patient == null){
                throw new EntryNotFoundException("Patient not found");
            }
            return modelMapper.map(patient, GetPatientByIdDTO.class);
        } catch (EntryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving Patient: " + e.getMessage());
        }
    }


}
