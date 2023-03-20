package com.compliance.documentvalidator.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentValidatorException extends RuntimeException {

    private HttpStatusCode statusCode;
    private String message;
}
