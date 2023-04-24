package com.compliance.documentvalidator.controller;

import com.compliance.documentvalidator.controller.DocumentValidatorController;
import com.compliance.documentvalidator.dto.*;
import com.compliance.documentvalidator.service.DocumentValidatorService;
import com.compliance.documentvalidator.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.net.URISyntaxException;

@ExtendWith(MockitoExtension.class)
public class DocumentValidatorControllerTest {

    @InjectMocks
    private DocumentValidatorController documentValidatorController;

    @Mock
    private DocumentValidatorService documentValidatorService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Users users;

    @Mock
    private DocGeneratorPayload docGeneratorPayload;

    @Mock
    private InitiatorPayload initiatorPayload;

    @Mock
    private Document document;

    @Mock
    private DocumentValidatorResponse documentValidatorResponse;

    @Test
    public void registerUserTest() {

        ResponseEntity<String> registerUserResponse = ResponseEntity.ok("User added successfully...!!!");
        Mockito.lenient().when(documentValidatorService.registerUser(users)).thenReturn(registerUserResponse);
        Mockito.lenient().when(documentValidatorController.registerUser(users)).thenReturn(registerUserResponse);
    }

    @Test
    public void validateInvoiceTest() throws URISyntaxException {

        ResponseEntity<DocumentValidatorResponse> validateInvoiceResponse = ResponseEntity.ok(documentValidatorResponse);
        Mockito.lenient().when(initiatorPayload.getSender()).thenReturn("sender");
        Mockito.lenient().when(initiatorPayload.getDocument()).thenReturn(document);
        Mockito.lenient().when(documentValidatorService.validateInvoice(docGeneratorPayload)).thenReturn(validateInvoiceResponse);
        Mockito.lenient().when(documentValidatorController.validateInvoice(initiatorPayload)).thenReturn(validateInvoiceResponse);
    }
}
