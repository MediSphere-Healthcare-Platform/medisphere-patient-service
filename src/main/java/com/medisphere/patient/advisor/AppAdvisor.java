package com.medisphere.patient.advisor;

import com.medisphere.patient.exception.EntryNotFoundException;
import com.medisphere.patient.exception.FileFormatNotSupportedException;
import com.medisphere.patient.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppAdvisor {

    @ExceptionHandler(EntryNotFoundException.class)
    public ResponseEntity<StandardResponse> handleEntryNotFoundException(EntryNotFoundException e) {
        return new ResponseEntity<>(
                new StandardResponse(404, e.getMessage(), null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(FileFormatNotSupportedException.class)
    public ResponseEntity<StandardResponse> handleFileFormatNotSupportedException(FileFormatNotSupportedException e) {
        return new ResponseEntity<>(
                new StandardResponse(400, e.getMessage(), null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(
                new StandardResponse(400, "Validation Failed", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse> handleGeneralException(Exception e) {
        return new ResponseEntity<>(
                new StandardResponse(500, "Internal Server Error: " + e.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
