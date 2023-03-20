package com.compliance.documentgenerator.exception.handler;

import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DocGeneratorExceptionHandler {

    @ExceptionHandler(value = DocumentGeneratorException.class)
    public final ResponseEntity<String> docGeneratorExceptionHandler(DocumentGeneratorException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
