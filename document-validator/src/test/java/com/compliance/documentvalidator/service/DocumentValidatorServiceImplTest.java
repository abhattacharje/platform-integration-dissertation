package com.compliance.documentvalidator.service;

import com.compliance.documentvalidator.configuration.PropertyConfiguration;
import com.compliance.documentvalidator.dto.DocGeneratorPayload;
import com.compliance.documentvalidator.dto.Document;
import com.compliance.documentvalidator.dto.DocumentValidatorResponse;
import com.compliance.documentvalidator.dto.Users;
import com.compliance.documentvalidator.entity.UserCredentials;
import com.compliance.documentvalidator.exception.DocumentValidatorException;
import com.compliance.documentvalidator.repository.UserCredentialsRepository;
import com.compliance.documentvalidator.service.handler.DocumentValidatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class DocumentValidatorServiceImplTest {

    @InjectMocks
    private DocumentValidatorServiceImpl documentValidatorService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PropertyConfiguration propertyConfiguration;

    @Mock
    private DocGeneratorPayload docGeneratorPayload;

    @Mock
    private Document document;

    @Mock
    private HttpClientErrorException httpClientErrorException;

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Mock
    private UserCredentials userCredentials;

    @Mock
    private Users users;

    @Test
    public void validateInvoicePositiveFlowTest() throws URISyntaxException {

        DocumentValidatorResponse documentValidatorResponse = new DocumentValidatorResponse("Company Name", "Validation Response");
        ResponseEntity<DocumentValidatorResponse> validateInvoiceResponse = ResponseEntity.ok(documentValidatorResponse);
        Mockito.lenient().when(docGeneratorPayload.getDocumnet()).thenReturn(document);
        Mockito.lenient().when(document.getContent()).thenReturn("Content");
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(DocumentValidatorResponse.class))).thenReturn(validateInvoiceResponse);
        assertEquals(documentValidatorService.validateInvoice(docGeneratorPayload).getStatusCode(), validateInvoiceResponse.getStatusCode());
    }

    @Test
    public void validateInvoiceNegativeFlowTest() throws URISyntaxException {

        Mockito.lenient().when(docGeneratorPayload.getDocumnet()).thenReturn(document);
        Mockito.lenient().when(document.getContent()).thenReturn("Content");
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(DocumentValidatorResponse.class))).thenThrow(httpClientErrorException);
        DocumentValidatorException thrown = assertThrows(
                DocumentValidatorException.class,
                () -> documentValidatorService.validateInvoice(docGeneratorPayload)
        );
    }

    @Test
    public void registerUserPositiveFlowTest() {

        ResponseEntity<String> registerUserResponse = ResponseEntity.ok("User added successfully...!!!");
        assertEquals(documentValidatorService.registerUser(users).getStatusCode(), registerUserResponse.getStatusCode());
    }

    @Test
    public void registerUserNegativeFlowTest() {

        Mockito.lenient().when(userCredentialsRepository.save(ArgumentMatchers.any(UserCredentials.class))).thenThrow(httpClientErrorException);
        DocumentValidatorException thrown = assertThrows(
                DocumentValidatorException.class,
                () -> documentValidatorService.registerUser(users)
        );
    }
}
