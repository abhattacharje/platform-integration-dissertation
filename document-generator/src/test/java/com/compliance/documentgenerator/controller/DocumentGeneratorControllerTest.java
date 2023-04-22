package com.compliance.documentgenerator.controller;

import com.compliance.documentgenerator.dto.Company;
import com.compliance.documentgenerator.dto.InvoicePayload;
import com.compliance.documentgenerator.dto.InvoiceResponse;
import com.compliance.documentgenerator.dto.User;
import com.compliance.documentgenerator.entity.CompanyDetails;
import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import com.compliance.documentgenerator.service.CompanyRegistrationService;
import com.compliance.documentgenerator.service.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DocumentGeneratorControllerTest {

    @InjectMocks
    private DocumentGeneratorController documentGeneratorController;

    @Mock
    private CompanyRegistrationService companyService;

    @Mock
    private UserRegistrationService userService;

    @Mock
    private User userPayload;

    @Mock
    private Company companyPayload;

    @Mock
    private HttpClientErrorException httpClientErrorException;

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Mock
    private CompanyDetails companyDetails;

    @Mock
    private InvoiceResponse invoiceResponse;

    @Mock
    private InvoicePayload invoicePayload;

    @Test
    public void testRegisterUserPositiveFlow() {

        ResponseEntity<String> registerUserResponse = ResponseEntity.ok("User Added Successfully...!!!");
        ResponseEntity<User> user = ResponseEntity.ok(userPayload);
        Mockito.when(userService.registerUser(userPayload)).thenReturn(user);
        assertEquals(documentGeneratorController.registerUser(userPayload).getStatusCode(), registerUserResponse.getStatusCode());
    }

    @Test
    public void testRegisterUserNegativeFlow() {

        Mockito.when(userService.registerUser(userPayload)).thenThrow(httpClientErrorException);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> documentGeneratorController.registerUser(userPayload)
        );
    }

    @Test
    public void testRegisterCompanyPositiveFlow() {

        ResponseEntity<String> registerCompanyResponse = ResponseEntity.ok("Company Added Successfully...!!!");
        ResponseEntity<Company> company = ResponseEntity.ok(companyPayload);
        Mockito.when(companyService.registerCompany(companyPayload)).thenReturn(company);
        assertEquals(documentGeneratorController.registerCompany(companyPayload).getStatusCode(), registerCompanyResponse.getStatusCode());
    }

    @Test
    public void testRegisterCompanyNegativeFlow() {

        Mockito.when(companyService.registerCompany(companyPayload)).thenThrow(httpClientErrorException);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> documentGeneratorController.registerCompany(companyPayload)
        );
    }

    @Test
    public void testGetCompanyByCompanyIdPositiveFlow() {

        ResponseEntity<CompanyDetails> getCompanyByCompanyIdResponse = ResponseEntity.ok(companyDetails);
        Mockito.when(companyService.getCompanyByCompanyId(ArgumentMatchers.anyString())).thenReturn(getCompanyByCompanyIdResponse);
        assertEquals(documentGeneratorController.getCompanyByCompanyId(Mockito.anyString()).getStatusCode(), getCompanyByCompanyIdResponse.getStatusCode());
    }

    @Test
    public void testGetCompanyByCompanyIdNegativeFlow() {

        Mockito.when(companyService.getCompanyByCompanyId(ArgumentMatchers.anyString())).thenThrow(httpClientErrorException);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> documentGeneratorController.getCompanyByCompanyId(Mockito.anyString())
        );
    }

    @Test
    public void testAcceptAndValidateInvoice() {

        ResponseEntity<InvoiceResponse> acceptAndValidateInvoiceResponse = ResponseEntity.ok(invoiceResponse);
        Mockito.when(companyService.acceptAndValidateInvoice(invoicePayload)).thenReturn(acceptAndValidateInvoiceResponse);
        assertEquals(documentGeneratorController.acceptAndValidateInvoice(invoicePayload).getStatusCode(), acceptAndValidateInvoiceResponse.getStatusCode());
    }
}
