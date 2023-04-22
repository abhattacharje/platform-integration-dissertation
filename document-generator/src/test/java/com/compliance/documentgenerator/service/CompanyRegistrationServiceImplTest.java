package com.compliance.documentgenerator.service;

import com.compliance.documentgenerator.dto.Company;
import com.compliance.documentgenerator.dto.Document;
import com.compliance.documentgenerator.dto.InvoicePayload;
import com.compliance.documentgenerator.dto.InvoiceResponse;
import com.compliance.documentgenerator.entity.CompanyDetails;
import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import com.compliance.documentgenerator.exception.InvoiceException;
import com.compliance.documentgenerator.repository.CompanyDetailsRepository;
import com.compliance.documentgenerator.service.handler.CompanyRegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CompanyRegistrationServiceImplTest {

    @InjectMocks
    private CompanyRegistrationServiceImpl companyRegistrationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CompanyDetails companyDetails;

    @Mock
    private HttpClientErrorException httpClientErrorException;

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Mock
    private Company companyPayload;

    @Mock
    private CompanyDetailsRepository companyDetailsRepository;

    @Mock
    private CompanyDetails company;

    @Mock
    private InvoicePayload invoicePayload;

    @Mock
    private InvoiceResponse invoiceResponse;

    @Mock
    private Document document;

    @Mock
    private InvoiceException invoiceException;

    @Test
    public void testRegisterCompanyPositiveFlow() {

        ResponseEntity<Company> companyRegistrationResponse = ResponseEntity.ok(companyPayload);
        assertEquals(companyRegistrationService.registerCompany(companyPayload).getStatusCode(), companyRegistrationResponse.getStatusCode());
    }

    @Test
    public void testGetCompanyByCompanyIdPositiveFlow() {

        Optional<CompanyDetails> companyInfo = Optional.of(company);
        Mockito.when(companyDetailsRepository.findByCompanyId(ArgumentMatchers.anyString())).thenReturn(companyInfo);
        ResponseEntity<CompanyDetails> companyDetails = ResponseEntity.ok(company);
        assertEquals(companyRegistrationService.getCompanyByCompanyId(Mockito.anyString()).getStatusCode(), companyDetails.getStatusCode());
    }

    @Test
    public void testGetCompanyByCompanyIdNegativeFlow() {

        Mockito.when(companyDetailsRepository.findByCompanyId(ArgumentMatchers.anyString())).thenThrow(httpClientErrorException);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> companyRegistrationService.getCompanyByCompanyId(ArgumentMatchers.anyString())
        );
        thrown.setStatusCode(HttpStatusCode.valueOf(404));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    }

    @Test
    public void testAcceptAndValidateInvoicePositiveFlow() {

        Base64.Encoder encoder = Base64.getEncoder();
        ResponseEntity<InvoiceResponse> acceptAndValidateInvoiceResponse = ResponseEntity.ok(invoiceResponse);
        String documentId = "123";
        String documentContent = encoder.encodeToString(documentId.getBytes());
        Optional<CompanyDetails> companyInfo = Optional.of(company);
        Mockito.when(companyDetailsRepository.findByCompanyId(invoicePayload.getSender())).thenReturn(companyInfo);
        Mockito.when(invoicePayload.getDocumnet()).thenReturn(document);
        invoicePayload.getDocumnet().setId(documentId);
        invoicePayload.getDocumnet().setContent(documentContent);
        Mockito.when(invoicePayload.getDocumnet().getId()).thenReturn(documentId);
        Mockito.when(invoicePayload.getDocumnet().getContent()).thenReturn(documentContent);
        assertEquals(companyRegistrationService.acceptAndValidateInvoice(invoicePayload).getStatusCode(), acceptAndValidateInvoiceResponse.getStatusCode());
    }

    @Test
    public void testAcceptAndValidateInvoiceNegative400Flow() {

        String documentId = "123";
        String documentContent = "abc";
        Optional<CompanyDetails> companyInfo = Optional.of(company);
        Mockito.when(companyDetailsRepository.findByCompanyId(invoicePayload.getSender())).thenReturn(companyInfo);
        Mockito.when(invoicePayload.getDocumnet()).thenReturn(document);
        invoicePayload.getDocumnet().setId(documentId);
        invoicePayload.getDocumnet().setContent(documentContent);
        Mockito.when(invoicePayload.getDocumnet().getId()).thenReturn(documentId);
        Mockito.when(invoicePayload.getDocumnet().getContent()).thenReturn(documentContent);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> companyRegistrationService.acceptAndValidateInvoice(invoicePayload)
        );
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
    }

    @Test
    public void testAcceptAndValidateInvoiceNegative404Flow() {

        Mockito.when(companyDetailsRepository.findByCompanyId(invoicePayload.getSender())).thenThrow(invoiceException);
        Mockito.when(invoiceException.getInvoiceResponse()).thenReturn(invoiceResponse);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> companyRegistrationService.acceptAndValidateInvoice(invoicePayload)
        );
        thrown.setStatusCode(HttpStatusCode.valueOf(404));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
    }
}
