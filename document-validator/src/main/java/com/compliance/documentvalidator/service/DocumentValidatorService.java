package com.compliance.documentvalidator.service;

import com.compliance.documentvalidator.dto.DocGeneratorPayload;
import com.compliance.documentvalidator.dto.DocumentValidatorResponse;
import com.compliance.documentvalidator.dto.Users;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface DocumentValidatorService {

    ResponseEntity<DocumentValidatorResponse> validateInvoice(DocGeneratorPayload docGeneratorPayload) throws URISyntaxException;

    ResponseEntity<String> registerUser(Users users);
}
