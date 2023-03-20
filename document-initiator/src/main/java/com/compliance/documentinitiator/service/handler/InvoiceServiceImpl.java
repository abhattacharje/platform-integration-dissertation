package com.compliance.documentinitiator.service.handler;

import com.compliance.documentinitiator.configuration.PropertyConfiguration;
import com.compliance.documentinitiator.dto.DocumentInitiatorResponse;
import com.compliance.documentinitiator.dto.InitiatorPayload;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.Invoices;
import com.compliance.documentinitiator.exception.DocumentInitiatorException;
import com.compliance.documentinitiator.repository.InvoicesRepository;
import com.compliance.documentinitiator.service.InvoicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoicesService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertyConfiguration propertyConfiguration;

    @Autowired
    private InvoicesRepository invoicesRepository;

    @Override
    public ResponseEntity<DocumentInitiatorResponse> validateInvoice(InitiatorPayload initiatorPayload) throws URISyntaxException {

        String tokenGeneratorApi = propertyConfiguration.getDocValidatorBaseUrl() + propertyConfiguration.getDocValidatorTokenGeneratorApi();
        String invoiceValidationApi = propertyConfiguration.getDocValidatorBaseUrl() + propertyConfiguration.getDocValidatorValidationApi();

        HttpHeaders tokenGeneratorHeaders = new HttpHeaders();
        tokenGeneratorHeaders.setContentType(MediaType.APPLICATION_JSON);
        tokenGeneratorHeaders.setAccept(new ArrayList<>(Arrays.asList(MediaType.APPLICATION_JSON)));
        HttpEntity<Users> tokenGenerationInput = new HttpEntity<>(initiatorPayload.getUsers(), tokenGeneratorHeaders);
        ResponseEntity<String> bearerToken;
        ResponseEntity<DocumentInitiatorResponse> validationResponse;

        Invoices invoice = new Invoices();

        try {
            log.info("Generating the Bearer token from Document Validator API");
            bearerToken = restTemplate.exchange(new URI(tokenGeneratorApi), HttpMethod.POST, tokenGenerationInput, String.class);
            log.info("Bearer token generated");

            HttpHeaders validationApiHeader = new HttpHeaders();
            validationApiHeader.setContentType(MediaType.APPLICATION_JSON);
            validationApiHeader.setAccept(new ArrayList<>(Arrays.asList(MediaType.APPLICATION_JSON)));
            validationApiHeader.set("Authorization", "Bearer "+bearerToken.getBody());
            HttpEntity<InitiatorPayload> validationInput = new HttpEntity<>(initiatorPayload, validationApiHeader);

            log.info("Calling Document Validator API to validate the invoice");
            validationResponse = restTemplate.exchange(new URI(invoiceValidationApi), HttpMethod.POST, validationInput, DocumentInitiatorResponse.class);
            log.info("Invoice validated by Document Generator through Document Validator");

            String invoiceId = UUID.randomUUID().toString();
            invoice.setInvoiceId(invoiceId);
            invoice.setCountry(initiatorPayload.getCountry());
            invoice.setCountryCode(initiatorPayload.getCountryCode());
            invoice.setSender(initiatorPayload.getSender());
            invoice.setDocument(initiatorPayload.getDocument());
            invoice.setInvoiceStatus(validationResponse.getBody().getValidationMessage());

            invoicesRepository.save(invoice);
            log.info("Saving the invoice validated in the DB");
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {

            if(ex.getMessage().contains("Invoice is InValid")) {
                String invoiceId = UUID.randomUUID().toString();
                invoice.setInvoiceId(invoiceId);
                invoice.setCountry(initiatorPayload.getCountry());
                invoice.setCountryCode(initiatorPayload.getCountryCode());
                invoice.setSender(initiatorPayload.getSender());
                invoice.setDocument(initiatorPayload.getDocument());
                invoice.setInvoiceStatus("Invoice is InValid");

                invoicesRepository.save(invoice);
                log.info("Invoice saved but with status -> "+invoice.getInvoiceStatus());
            }
            else if(ex.getMessage().contains("Company Not Present.")) {
                String invoiceId = UUID.randomUUID().toString();
                invoice.setInvoiceId(invoiceId);
                invoice.setCountry(initiatorPayload.getCountry());
                invoice.setCountryCode(initiatorPayload.getCountryCode());
                invoice.setSender(initiatorPayload.getSender());
                invoice.setDocument(initiatorPayload.getDocument());
                invoice.setInvoiceStatus("Company Not Present.");

                invoicesRepository.save(invoice);
                log.info("Invoice saved but with status -> "+invoice.getInvoiceStatus());
            }

            throw new DocumentInitiatorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(validationResponse.getBody());
    }

    @Override
    public ResponseEntity<List<Invoices>> getAllInvoices() {
        List<Invoices> listOfInvoices;
        try {
            log.info("Fetching all the invoices");
            listOfInvoices = invoicesRepository.findAll();
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error while fetching the invoices");
            throw new DocumentInitiatorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(listOfInvoices);
    }
}
