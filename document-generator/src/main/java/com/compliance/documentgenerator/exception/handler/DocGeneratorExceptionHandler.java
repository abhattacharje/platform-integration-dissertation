package com.compliance.documentgenerator.exception.handler;

import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DocGeneratorExceptionHandler {

    @ExceptionHandler(value = DocumentGeneratorException.class)
    public final ResponseEntity<String> docGeneratorExceptionHandler(DocumentGeneratorException exception) {
        log.error("Exception occured with message -> "+exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
