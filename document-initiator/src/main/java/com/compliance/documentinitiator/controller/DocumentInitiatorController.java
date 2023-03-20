package com.compliance.documentinitiator.controller;

import com.compliance.documentinitiator.dto.DocumentInitiatorResponse;
import com.compliance.documentinitiator.dto.InitiatorPayload;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.Invoices;
import com.compliance.documentinitiator.service.InvoicesService;
import com.compliance.documentinitiator.service.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/documentInitiator")
public class DocumentInitiatorController {

    @Autowired
    private InvoicesService invoicesService;

    @Autowired
    private UserCredentialsService userCredentialsService;

    @PostMapping("/userRegister")
    public ResponseEntity<String> registerUser(@RequestBody Users users) throws URISyntaxException {
        return userCredentialsService.registerUser(users);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<DocumentInitiatorResponse> validateInvoice(@RequestBody InitiatorPayload initiatorPayload) throws URISyntaxException {
        return invoicesService.validateInvoice(initiatorPayload);
    }

    @GetMapping("/fetchAllInvoices")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Invoices>> getAllInvoices() {
        return invoicesService.getAllInvoices();
    }
}
