package com.compliance.documentvalidator.exception.handler;

import com.compliance.documentvalidator.exception.DocumentValidatorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DocValidatorExceptionHandler {

    @ExceptionHandler(value = DocumentValidatorException.class)
    public final ResponseEntity<String> docValidatorExceptionHandler(DocumentValidatorException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
