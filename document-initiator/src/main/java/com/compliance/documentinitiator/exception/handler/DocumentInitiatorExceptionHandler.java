package com.compliance.documentinitiator.exception.handler;

import com.compliance.documentinitiator.exception.DocumentInitiatorException;
import com.compliance.documentinitiator.exception.UserLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DocumentInitiatorExceptionHandler {

    @ExceptionHandler(value = DocumentInitiatorException.class)
    public final ResponseEntity<String> docValidatorExceptionHandler(DocumentInitiatorException exception) {
        log.error("Exception occured with message -> "+exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), exception.getStatusCode());
    }
}
