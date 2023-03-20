package com.compliance.documentinitiator.service;

import com.compliance.documentinitiator.dto.DocumentInitiatorResponse;
import com.compliance.documentinitiator.dto.InitiatorPayload;
import com.compliance.documentinitiator.entity.Invoices;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;

public interface InvoicesService {

    ResponseEntity<DocumentInitiatorResponse> validateInvoice(InitiatorPayload initiatorPayload) throws URISyntaxException;

    ResponseEntity<List<Invoices>> getAllInvoices();
}
