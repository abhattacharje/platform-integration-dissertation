package com.compliance.documentinitiator.service;

import com.compliance.documentinitiator.configuration.PropertyConfiguration;
import com.compliance.documentinitiator.dto.DocumentInitiatorResponse;
import com.compliance.documentinitiator.dto.Documents;
import com.compliance.documentinitiator.dto.InitiatorPayload;
import com.compliance.documentinitiator.dto.TokenGenerationPayload;
import com.compliance.documentinitiator.entity.Invoices;
import com.compliance.documentinitiator.exception.DocumentInitiatorException;
import com.compliance.documentinitiator.repository.InvoicesRepository;
import com.compliance.documentinitiator.service.handler.InvoiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PropertyConfiguration propertyConfiguration;

    @Mock
    private InvoicesRepository invoicesRepository;

    @Mock
    private DocumentInitiatorResponse documentInitiatorResponse;

    @Mock
    private InitiatorPayload initiatorPayload;

    @Mock
    private Invoices invoice;

    @Mock
    private TokenGenerationPayload tokenGenerationPayload;

    @Mock
    private Documents document;

    @Mock
    private HttpClientErrorException httpClientErrorException;

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Test
    public void testValidateInvoicePositiveFlow() throws URISyntaxException {

        ResponseEntity<DocumentInitiatorResponse> validateInvoiceResponse = ResponseEntity.ok(documentInitiatorResponse);
        ResponseEntity<String> bearerTokenResponse = ResponseEntity.ok("bearer token");
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(bearerTokenResponse);
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(DocumentInitiatorResponse.class))).thenReturn(validateInvoiceResponse);
        assertEquals(invoiceService.validateInvoice(initiatorPayload).getStatusCode(), validateInvoiceResponse.getStatusCode());
    }

    @Test
    public void testValidateInvoiceNegativeFlowInvoiceInvalid() throws URISyntaxException {

        Mockito.lenient().when(httpClientErrorException.getMessage()).thenReturn("Invoice is InValid");
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(httpClientErrorException);
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(DocumentInitiatorResponse.class))).thenThrow(httpClientErrorException);
        DocumentInitiatorException thrown = assertThrows(
                DocumentInitiatorException.class,
                () -> invoiceService.validateInvoice(initiatorPayload)
        );
    }

    @Test
    public void testValidateInvoiceNegativeFlowCompanyNotPresent() throws URISyntaxException {

        Mockito.lenient().when(httpClientErrorException.getMessage()).thenReturn("Company Not Present.");
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(httpClientErrorException);
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(DocumentInitiatorResponse.class))).thenThrow(httpClientErrorException);
        DocumentInitiatorException thrown = assertThrows(
                DocumentInitiatorException.class,
                () -> invoiceService.validateInvoice(initiatorPayload)
        );
    }

    @Test
    public void testGetAllInvoicesPositiveFlow() {

        List<Invoices> listOfInvoices = List.of(invoice);
        ResponseEntity<List<Invoices>> getAllInvoicesResponse = ResponseEntity.ok(listOfInvoices);
        Mockito.when(invoicesRepository.findAll()).thenReturn(listOfInvoices);
        assertEquals(invoiceService.getAllInvoices().getStatusCode(), getAllInvoicesResponse.getStatusCode());
    }

    @Test
    public void testGetAllInvoicesNegativeFlow() {

        Mockito.when(invoicesRepository.findAll()).thenThrow(httpClientErrorException);
        DocumentInitiatorException thrown = assertThrows(
                DocumentInitiatorException.class,
                () -> invoiceService.getAllInvoices()
        );
    }
}
