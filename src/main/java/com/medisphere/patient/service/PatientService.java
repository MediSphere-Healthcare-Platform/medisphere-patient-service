package com.medisphere.patient.service;

import com.medisphere.patient.dto.request.CreatePatientDTO;
import com.medisphere.patient.dto.request.DeletePatientDTO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;


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

    public String createPatient(@Validated CreatePatientDTO createPatientDTO, MultipartFile profileImage){
        try{
            String lastId = patientRepository.findLastPatientId();
            String newPatientId;
            if(lastId == null){
                newPatientId = "P001";
            }else{
                // Extract trailing digits from any prefix format (e.g. P005, PAT005)
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)$").matcher(lastId);
                if (!matcher.find()) {
                    throw new RuntimeException("Cannot parse last patient ID: " + lastId);
                }
                int number = Integer.parseInt(matcher.group(1));
                number++;
                newPatientId = String.format("P%03d", number);
            }

            createPatientDTO.setPatientId(newPatientId);

            if(createPatientDTO.getMsUserId() == null){
                return "msUserId Can't be null";
            }

            PatientEntity checkPatientExists = patientRepository.checkPatientByMsUserId(createPatientDTO.getMsUserId());

            if(checkPatientExists != null){
                return "According to given msUserId patient already exists";
            }

            if(createPatientDTO.getAllergies() == null || createPatientDTO.getAllergies().isEmpty()){
                createPatientDTO.setAllergies("No Allergies Inserted yet");
            }
            if(createPatientDTO.getBloodGroup() == null || createPatientDTO.getBloodGroup().isEmpty()){
                createPatientDTO.setBloodGroup("No Blood Group Inserted yet");
            }
            if(createPatientDTO.getChronicConditions() == null || createPatientDTO.getChronicConditions().isEmpty()){
                createPatientDTO.setChronicConditions("No Chronic Conditions Inserted yet");
            }

            // Handle optional profile image upload
            if(profileImage != null && !profileImage.isEmpty()){
                String imageUrl = cloudinaryService.uploadProfileImage(profileImage);
                createPatientDTO.setProfileImageUrl(imageUrl);
            } else {
                createPatientDTO.setProfileImageUrl(null);
            }

            // Build entity manually to avoid ModelMapper confusing
            // String 'patientId' in DTO with Integer 'id' in Entity
            PatientEntity patientEntity = new PatientEntity();
            patientEntity.setPatientId(createPatientDTO.getPatientId());
            patientEntity.setMsUserId(createPatientDTO.getMsUserId());
            patientEntity.setFirstName(createPatientDTO.getFirstName());
            patientEntity.setLastName(createPatientDTO.getLastName());
            patientEntity.setDateOfBirth(createPatientDTO.getDateOfBirth());
            patientEntity.setGender(createPatientDTO.getGender());
            patientEntity.setPhoneNumber(createPatientDTO.getPhoneNumber());
            patientEntity.setAddress(createPatientDTO.getAddress());
            patientEntity.setBloodGroup(createPatientDTO.getBloodGroup());
            patientEntity.setAllergies(createPatientDTO.getAllergies());
            patientEntity.setChronicConditions(createPatientDTO.getChronicConditions());
            patientEntity.setProfileImageUrl(createPatientDTO.getProfileImageUrl());
            patientEntity.setCreatedAt(Instant.now());
            patientEntity.setUpdatedAt(Instant.now());
            patientEntity.setStatus("ACTIVE");

            patientRepository.save(patientEntity);

            return newPatientId;

        }catch(EntryNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Failed to create Patient: " + e.getMessage());
        }
    }


    public String deletePatient(@Validated DeletePatientDTO deletePatientDTO){

        if(deletePatientDTO.getPatientId() == null){
            return "PatientId can't be null";
        }

        try{
            PatientEntity patient = patientRepository.findByPatientId(deletePatientDTO.getPatientId());

            if(patient == null){
                return "According to given patient ID patient doesn't exists";
            }

            // Delete profile image from Cloudinary if it exists
            if (patient.getProfileImageUrl() != null && !patient.getProfileImageUrl().isEmpty()) {
                cloudinaryService.deleteImage(patient.getProfileImageUrl());
            }

            patientRepository.deletePatientByPatientId(deletePatientDTO.getPatientId());
            return "Deleted";

        }catch(Exception e){
            throw new RuntimeException("Failed to delete Patient: " + e.getMessage());
        }
    }


}
