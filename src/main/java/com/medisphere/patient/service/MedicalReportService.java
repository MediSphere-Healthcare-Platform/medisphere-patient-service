package com.medisphere.patient.service;

import com.medisphere.patient.dto.request.DeleteAllReportsByPatientIdReqDTO;
import com.medisphere.patient.dto.request.DeleteMedicalReportByIdReqDTO;
import com.medisphere.patient.dto.request.GetPatientByIdReqDTO;
import com.medisphere.patient.dto.request.UploadMedicalReportReqDTO;
import com.medisphere.patient.dto.response.GetAllMedicalReportsByPatientIdDTO;
import com.medisphere.patient.dto.response.UploadMedicalReportResDTO;
import com.medisphere.patient.entity.MedisphereMedicalReportEntity;
import com.medisphere.patient.entity.PatientEntity;
import com.medisphere.patient.exception.EntryNotFoundException;
import com.medisphere.patient.exception.FileFormatNotSupportedException;
import com.medisphere.patient.repository.MedicalReportRepository;
import com.medisphere.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalReportService {

    private final MedicalReportRepository medicalReportRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

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

    public UploadMedicalReportResDTO uploadMedicalReport(UploadMedicalReportReqDTO dto, MultipartFile file) {
        try {
            // Check patient exists
            PatientEntity patient = patientRepository.findByPatientId(dto.getPatientId());
            if (patient == null) {
                throw new EntryNotFoundException("According to given patient ID patient doesn't exists");
            }

            // Check if accurately file is an image
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new FileFormatNotSupportedException("Only image files (JPG, PNG, etc.) are allowed for medical reports");
            }

            // Generate report ID (Format: R001, R002...)
            String lastId = medicalReportRepository.findLastReportId();
            String newReportId;
            if (lastId == null) {
                newReportId = "R001";
            } else {
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(\\d+)$").matcher(lastId);
                if (!matcher.find()) {
                    throw new RuntimeException("Cannot parse last report ID: " + lastId);
                }
                int number = Integer.parseInt(matcher.group(1));
                number++;
                newReportId = String.format("R%03d", number);
            }

            // Upload report to Cloudinary
            String fileUrl = cloudinaryService.uploadReport(file);

            // Save to DB
            MedisphereMedicalReportEntity reportEntity = new MedisphereMedicalReportEntity();
            reportEntity.setReportId(newReportId);
            reportEntity.setPatient(dto.getPatientId());
            reportEntity.setDoctorId(dto.getDoctorId());
            reportEntity.setReportName(dto.getReportName());
            reportEntity.setReportType(dto.getReportType());
            reportEntity.setFileUrl(fileUrl);
            reportEntity.setFileType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
            reportEntity.setDescription(dto.getDescription());
            reportEntity.setUploadedAt(Instant.now());

            medicalReportRepository.save(reportEntity);

            return modelMapper.map(reportEntity, UploadMedicalReportResDTO.class);

        } catch (EntryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload report: " + e.getMessage());
        }
    }

    public String deleteMedicalReportById(DeleteMedicalReportByIdReqDTO dto) {
        try {
            MedisphereMedicalReportEntity report = medicalReportRepository.findByReportId(dto.getReportId());
            if (report == null) {
                throw new EntryNotFoundException("According to given report ID report doesn't exists");
            }

            // Delete from Cloudinary
            cloudinaryService.deleteImage(report.getFileUrl());

            // Delete from DB
            medicalReportRepository.deleteByReportId(dto.getReportId());

            return "Deleted Successfully";
        } catch (EntryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete report: " + e.getMessage());
        }
    }

    public String deleteAllMedicalReportsByPatientId(DeleteAllReportsByPatientIdReqDTO dto) {
        try {
            List<MedisphereMedicalReportEntity> reports = medicalReportRepository.findAllByPatient(dto.getPatientId());
            if (reports == null || reports.isEmpty()) {
                return "No reports found for the given patient ID";
            }

            // Delete each from Cloudinary
            for (MedisphereMedicalReportEntity report : reports) {
                cloudinaryService.deleteImage(report.getFileUrl());
            }

            // Delete all from DB
            medicalReportRepository.deleteAllByPatientId(dto.getPatientId());

            return "All reports deleted successfully for patient: " + dto.getPatientId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete all reports: " + e.getMessage());
        }
    }

    public List<GetAllMedicalReportsByPatientIdDTO> getAllMedicalReportByDoctorId(String doctorId) {
        try {
            List<MedisphereMedicalReportEntity> doctorReports = medicalReportRepository.findAllByDoctorId(doctorId);
            
            if (doctorReports == null || doctorReports.isEmpty()) {
                throw new EntryNotFoundException("there have no reports uploaded for given doctor id");
            }
            
            List<GetAllMedicalReportsByPatientIdDTO> dtoList = modelMapper.map(doctorReports, new TypeToken<List<GetAllMedicalReportsByPatientIdDTO>>() {}.getType());
            
            // Enrich with patient names
            for (GetAllMedicalReportsByPatientIdDTO dto : dtoList) {
                PatientEntity patient = patientRepository.findByPatientId(dto.getPatient());
                if (patient != null) {
                    dto.setPatientName(patient.getFirstName() + " " + patient.getLastName());
                } else {
                    dto.setPatientName("Unknown Patient (" + dto.getPatient() + ")");
                }
            }
            
            return dtoList;
        } catch (EntryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch reports for doctor: " + e.getMessage());
        }
    }
}
