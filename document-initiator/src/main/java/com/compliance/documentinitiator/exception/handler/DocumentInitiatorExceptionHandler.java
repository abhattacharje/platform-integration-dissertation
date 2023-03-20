package com.compliance.documentinitiator.exception.handler;

import com.compliance.documentinitiator.exception.DocumentInitiatorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DocumentInitiatorExceptionHandler {

    @ExceptionHandler(value = DocumentInitiatorException.class)
    public final ResponseEntity<String> docValidatorExceptionHandler(DocumentInitiatorException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}