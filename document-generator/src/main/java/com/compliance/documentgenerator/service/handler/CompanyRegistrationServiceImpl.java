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

        companyDetails.setCompanyName(companyPayload.getCompanyName());
        companyDetails.setUserName(companyPayload.getUserName());
        companyDetails.setPassword(passwordEncoder.encode(companyPayload.getPassword()));
        companyDetails.setCompanyId(companyId);

        try {
            companyDetailsRepository.save(companyDetails);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
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
            }
            else {
                throw new HttpClientErrorException(HttpStatusCode.valueOf(404), "Company Not Found");
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
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
            if(companyInfo.isPresent()) {

                company = companyInfo.get();

                String documentId = invoicePayload.getDocumnet().getId();
                String documentContent = invoicePayload.getDocumnet().getContent();

                Base64.Decoder decoder = Base64.getDecoder();
                documentContent = new String(decoder.decode(documentContent));

                if(documentId.equals(documentContent)) {
                    invoiceResponse.setCompanyName(company.getCompanyName());
                    invoiceResponse.setValidationMessage("Invoice is Valid");
                }
                else {
                    invoiceResponse.setCompanyName(company.getCompanyName());
                    invoiceResponse.setValidationMessage("Invoice is InValid");
                    throw new InvoiceException(HttpStatusCode.valueOf(400), invoiceResponse);
                }
            }
            else {
                invoiceResponse.setCompanyName("<NOT PRESENT>");
                invoiceResponse.setValidationMessage("Company Not Present.");
                throw new InvoiceException(HttpStatusCode.valueOf(404), invoiceResponse);
            }
        }
        catch (InvoiceException ex) {
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getInvoiceResponse().getValidationMessage());
        }
        return ResponseEntity.ok(invoiceResponse);
    }
}
