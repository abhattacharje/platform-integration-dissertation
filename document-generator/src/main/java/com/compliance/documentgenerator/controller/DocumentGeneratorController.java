package com.compliance.documentgenerator.controller;

import com.compliance.documentgenerator.dto.Company;
import com.compliance.documentgenerator.dto.InvoicePayload;
import com.compliance.documentgenerator.dto.InvoiceResponse;
import com.compliance.documentgenerator.dto.User;
import com.compliance.documentgenerator.entity.CompanyDetails;
import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import com.compliance.documentgenerator.service.CompanyRegistrationService;
import com.compliance.documentgenerator.service.UserRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestController
@RequestMapping("/docGenerator")
public class DocumentGeneratorController {

    @Autowired
    private CompanyRegistrationService companyService;

    @Autowired
    private UserRegistrationService userService;

    @PostMapping("/userRegister")
    public ResponseEntity<String> registerUser(@RequestBody User usersPayload) {

        try {
            ResponseEntity<User> user = userService.registerUser(usersPayload);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Cannot register the user. Exception occurred");
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getMessage());
        }
        log.info("User Registered successfully -> "+usersPayload.getUserName());
        return ResponseEntity.ok("User Added Successfully...!!!");
    }

    @PostMapping("/companyRegister")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> registerCompany(@RequestBody Company companyPayload) {

        try {
            ResponseEntity<Company> company = companyService.registerCompany(companyPayload);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Cannot register the company. Exception occurred");
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getMessage());
        }
        log.info("Company Registered successfully -> "+companyPayload.getCompanyName());
        return ResponseEntity.ok("Company Added Successfully...!!!");
    }

    @GetMapping("/fetchCompanyDetails")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CompanyDetails> getCompanyByCompanyId(@RequestParam(name = "companyId") String companyId) {

        ResponseEntity<CompanyDetails> companyDetails;
        try {
            companyDetails = companyService.getCompanyByCompanyId(companyId);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error in fetching company details with company id: "+companyId);
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getMessage());
        }
        log.info("Company Details fetched successfully -> "+companyId);
        return ResponseEntity.ok(companyDetails.getBody());
    }

    @PostMapping("/validateInvoice")
    public ResponseEntity<InvoiceResponse> acceptAndValidateInvoice(@RequestBody InvoicePayload invoicePayload) {

        ResponseEntity<InvoiceResponse> invoiceResponse;
        log.info("[Inside DocumentGeneratorController] validate invoice");
        invoiceResponse = companyService.acceptAndValidateInvoice(invoicePayload);
        log.info("[Inside DocumentGeneratorController] invoice validated");
        return ResponseEntity.ok(invoiceResponse.getBody());
    }
}
