package com.medisphere.patient.util;

public class Endpoint {
    public static final String GET_ALL_PATIENT_FOR_ADMIN = "/getAllPatientForAdmin";
    public static final String GET_PATIENT_BY_ID = "/getPatientById/{id}";
    public static final String UPDATE_PATIENT_DETAILS = "/updatePatientDetails/{id}";
    public static final String GET_ALL_DOCTORS_FOR_PATIENT = "/getAllDoctors";
    public static final String CREATE_PATIENT = "/createPatient";
    public static final String DELETE_PATIENT = "/deletePatient/{pid}";
    public static final String BOOK_AN_APPOINTMENT = "appointments/bookAppointment";
    public static final String UPDATE_APPOINTMENT = "appointments/updateAppointment";
    public static final String DELETE_APPOINTMENT = "appointments/cancel/{appointmentReferenceId}";
    public static final String GET_PATIENT_REPORTS_BY_ID = "/getPatientReportsByPatientId/{pid}";
    public static final String UPLOAD_MEDICAL_REPORT = "/uploadMedicalReport";
    public static final String DELETE_MEDICAL_REPORT_BY_ID = "/deleteMedicalReport/{reportId}";
    public static final String DELETE_ALL_REPORTS_BY_PATIENT_ID = "/deleteAllMedicalReportsByPatientId/{pid}";
}
