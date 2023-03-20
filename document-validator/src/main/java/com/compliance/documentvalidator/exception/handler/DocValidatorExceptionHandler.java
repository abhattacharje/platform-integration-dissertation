package com.compliance.documentvalidator.exception.handler;

import com.compliance.documentvalidator.exception.DocumentValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DocValidatorExceptionHandler {

    @ExceptionHandler(value = DocumentValidatorException.class)
    public final ResponseEntity<String> docValidatorExceptionHandler(DocumentValidatorException exception) {
        log.error("Exception occured with message -> "+exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
