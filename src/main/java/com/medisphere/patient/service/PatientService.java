package com.medisphere.patient.service;

import com.medisphere.patient.client.AuthClient;
import com.medisphere.patient.dto.request.CreatePatientDTO;
import com.medisphere.patient.dto.request.DeletePatientDTO;
import com.medisphere.patient.dto.request.GetPatientByIdReqDTO;
import com.medisphere.patient.dto.request.UpdatePatientDetailsReqDTO;
import com.medisphere.patient.dto.response.GetPatientByIdDTO;
import com.medisphere.patient.dto.response.GetAllPatientsForAdminDTO;
import com.medisphere.patient.dto.response.UpdatePatientDetailsResDTO;
import com.medisphere.patient.entity.PatientEntity;
import com.medisphere.patient.exception.EntryNotFoundException;
import com.medisphere.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final AuthClient authClient;


    public Long getMaxMsUserId() {
        return patientRepository.findMaxMsUserId();
    }

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
                throw new RuntimeException("msUserId Can't be null");
            }

            PatientEntity checkPatientExists = patientRepository.checkPatientByMsUserId(createPatientDTO.getMsUserId());

            if(checkPatientExists != null){
                throw new RuntimeException("According to given msUserId patient already exists");
            }

            // Standardize empty/null fields
            setDefaultsForEmptyFields(createPatientDTO);

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
            throw new RuntimeException("PatientId can't be null");
        }

        try{
            PatientEntity patient = patientRepository.findByPatientId(deletePatientDTO.getPatientId());

            if(patient == null){
                throw new EntryNotFoundException("According to given patient ID patient doesn't exists");
            }

            // Delete profile image from Cloudinary if it exists
            if (patient.getProfileImageUrl() != null && !patient.getProfileImageUrl().isEmpty()) {
                cloudinaryService.deleteImage(patient.getProfileImageUrl());
            }

            patientRepository.deletePatientByPatientId(deletePatientDTO.getPatientId());
            
            // Sync deletion with Auth Service
            try {
                authClient.deleteUserByMsUserId(patient.getMsUserId());
            } catch (Exception e) {
                System.err.println("Failed to sync deletion with Auth Service: " + e.getMessage());
            }
            
            return "Deleted";

        }catch(Exception e){
            throw new RuntimeException("Failed to delete Patient: " + e.getMessage());
        }
    }

    public String deletePatientByMsUserId(String msUserId) {
        try {
            PatientEntity patient = patientRepository.checkPatientByMsUserId(msUserId);
            if (patient == null) {
                return "Patient not found for MS User ID: " + msUserId;
            }
            
            if (patient.getProfileImageUrl() != null && !patient.getProfileImageUrl().isEmpty()) {
                cloudinaryService.deleteImage(patient.getProfileImageUrl());
            }
            
            patientRepository.deletePatientByPatientId(patient.getPatientId());

            // Sync deletion with Auth Service
            try {
                authClient.deleteUserByMsUserId(msUserId);
            } catch (Exception e) {
                System.err.println("Failed to sync deletion with Auth Service: " + e.getMessage());
            }

            return "Deleted";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Patient by MS User ID: " + e.getMessage());
        }
    }

    public UpdatePatientDetailsResDTO updatePatient(String patientId, UpdatePatientDetailsReqDTO dto, MultipartFile profileImage) {
        try {
            PatientEntity patient = patientRepository.findByPatientId(patientId);
            if (patient == null) {
                throw new EntryNotFoundException("Patient not found with ID: " + patientId);
            }

            // Handle profile image update
            if (profileImage != null && !profileImage.isEmpty()) {
                // Delete old image if it exists
                if (patient.getProfileImageUrl() != null && !patient.getProfileImageUrl().isEmpty()) {
                    cloudinaryService.deleteImage(patient.getProfileImageUrl());
                }
                // Upload new image
                String newImageUrl = cloudinaryService.uploadProfileImage(profileImage);
                patient.setProfileImageUrl(newImageUrl);
            }

            // Update allowed fields
            patient.setDateOfBirth(dto.getDateOfBirth());
            patient.setGender(dto.getGender());
            patient.setPhoneNumber(dto.getPhoneNumber());
            patient.setAddress(dto.getAddress());
            
            // Handle optional fields with defaults
            patient.setBloodGroup((dto.getBloodGroup() == null || dto.getBloodGroup().isEmpty()) ? "No Blood Group Inserted yet" : dto.getBloodGroup());
            patient.setAllergies((dto.getAllergies() == null || dto.getAllergies().isEmpty()) ? "No Allergies Inserted yet" : dto.getAllergies());
            patient.setChronicConditions((dto.getChronicConditions() == null || dto.getChronicConditions().isEmpty()) ? "No Chronic Conditions Inserted yet" : dto.getChronicConditions());
            
            patient.setUpdatedAt(Instant.now());

            PatientEntity updatedPatient = patientRepository.save(patient);
            return modelMapper.map(updatedPatient, UpdatePatientDetailsResDTO.class);

        } catch (EntryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Patient: " + e.getMessage());
        }
    }

    private void setDefaultsForEmptyFields(CreatePatientDTO dto) {
        if (dto.getAllergies() == null || dto.getAllergies().isEmpty()) {
            dto.setAllergies("No Allergies Inserted yet");
        }
        if (dto.getBloodGroup() == null || dto.getBloodGroup().isEmpty()) {
            dto.setBloodGroup("No Blood Group Inserted yet");
        }
        if (dto.getChronicConditions() == null || dto.getChronicConditions().isEmpty()) {
            dto.setChronicConditions("No Chronic Conditions Inserted yet");
        }
    }

}
