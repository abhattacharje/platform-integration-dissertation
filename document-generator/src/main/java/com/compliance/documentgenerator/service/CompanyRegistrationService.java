package com.compliance.documentgenerator.service;

import com.compliance.documentgenerator.dto.Company;
import com.compliance.documentgenerator.dto.InvoicePayload;
import com.compliance.documentgenerator.dto.InvoiceResponse;
import com.compliance.documentgenerator.entity.CompanyDetails;
import org.springframework.http.ResponseEntity;

public interface CompanyRegistrationService {

    ResponseEntity<Company> registerCompany(Company companyPayload);

    ResponseEntity<CompanyDetails> getCompanyByCompanyId(String companyId);

    ResponseEntity<InvoiceResponse> acceptAndValidateInvoice(InvoicePayload invoicePayload);
}
