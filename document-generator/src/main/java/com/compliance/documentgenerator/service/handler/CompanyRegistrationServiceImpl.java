package com.compliance.documentgenerator.service.handler;

import com.compliance.documentgenerator.dto.Company;
import com.compliance.documentgenerator.dto.InvoicePayload;
import com.compliance.documentgenerator.dto.InvoiceResponse;
import com.compliance.documentgenerator.entity.CompanyDetails;
import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import com.compliance.documentgenerator.exception.InvoiceException;
import com.compliance.documentgenerator.repository.CompanyDetailsRepository;
import com.compliance.documentgenerator.service.CompanyRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CompanyRegistrationServiceImpl implements CompanyRegistrationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Override
    public ResponseEntity<Company> registerCompany(Company companyPayload) {

        CompanyDetails companyDetails = new CompanyDetails();
        String companyId = UUID.randomUUID().toString();
        log.info("companyId generated");

        companyDetails.setCompanyName(companyPayload.getCompanyName());
        companyDetails.setUserName(companyPayload.getUserName());
        companyDetails.setPassword(passwordEncoder.encode(companyPayload.getPassword()));
        companyDetails.setCompanyId(companyId);

        try {
            companyDetailsRepository.save(companyDetails);
            log.info("Company registered in DB");
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Exception occurred while registering company");
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(companyPayload);
    }

    @Override
    public ResponseEntity<CompanyDetails> getCompanyByCompanyId(String companyId) {

        CompanyDetails company = new CompanyDetails();
        try {
            Optional<CompanyDetails> companyInfo = companyDetailsRepository.findByCompanyId(companyId);
            if(companyInfo.isPresent()) {
                company = companyInfo.get();
                log.info("Company is present, fetching company details");
            }
            else {
                log.error("Company not present with companyId -> "+companyId);
                throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "Company Not Found");
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Exception occurred while fetching company details");
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(company);
    }

    @Override
    public ResponseEntity<InvoiceResponse> acceptAndValidateInvoice(InvoicePayload invoicePayload) {

        InvoiceResponse invoiceResponse = new InvoiceResponse();
        CompanyDetails company = new CompanyDetails();

        try {
            Optional<CompanyDetails> companyInfo = companyDetailsRepository.findByCompanyId(invoicePayload.getSender());
            log.info("Fetching the sender details");

            if(companyInfo.isPresent()) {

                company = companyInfo.get();
                log.info("Sender details fetched");

                String documentId = invoicePayload.getDocumnet().getId();
                String documentContent = invoicePayload.getDocumnet().getContent();
                log.info("Fetching invoice details");

                Base64.Decoder decoder = Base64.getDecoder();
                documentContent = new String(decoder.decode(documentContent));
                log.info("Base64 decoding the invoice content");

                if(documentId.equals(documentContent)) {
                    invoiceResponse.setCompanyName(company.getCompanyName());
                    invoiceResponse.setValidationMessage("Invoice is Valid");
                    log.info("Invoice is valid");
                }
                else {
                    invoiceResponse.setCompanyName(company.getCompanyName());
                    invoiceResponse.setValidationMessage("Invoice is InValid");
                    log.error("Invoice is invalid [Exception occurred] invoice id -> "+documentId);
                    throw new InvoiceException(HttpStatusCode.valueOf(400), invoiceResponse);
                }
            }
            else {
                invoiceResponse.setCompanyName("<NOT PRESENT>");
                invoiceResponse.setValidationMessage("Company Not Present.");
                log.error("Company not present [Exception occurred] company name -> "+companyInfo.get().getCompanyName());
                throw new InvoiceException(HttpStatusCode.valueOf(404), invoiceResponse);
            }
        }
        catch (InvoiceException ex) {
            log.error("Exception occurred while validating the invoice");
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getInvoiceResponse().getValidationMessage());
        }
        return ResponseEntity.ok(invoiceResponse);
    }
}
