package com.medisphere.patient.util;

public class Endpoint {
    public static final String GET_ALL_PATIENT_FOR_ADMIN = "/getAllPatientForAdmin";
    public static final String GET_PATIENT_BY_ID = "/getPatientById/{id}";
    public static final String UPDATE_PATIENT_DETAILS = "/updatePatientDetails/{id}";
    public static final String GET_ALL_DOCTORS_FOR_PATIENT = "/getAllDoctors";
    public static final String CREATE_PATIENT = "/createPatient";
    public static final String BOOK_AN_APPOINTMENT = "appointments/bookAppointment";
}
