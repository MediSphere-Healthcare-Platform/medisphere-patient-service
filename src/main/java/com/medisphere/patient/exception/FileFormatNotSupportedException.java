package com.medisphere.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileFormatNotSupportedException extends RuntimeException {
    public FileFormatNotSupportedException(String message) {
        super(message);
    }
}
