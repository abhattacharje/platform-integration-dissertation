package com.compliance.documentinitiator.controller;

import com.compliance.documentinitiator.dto.DocumentInitiatorResponse;
import com.compliance.documentinitiator.dto.InitiatorPayload;
import com.compliance.documentinitiator.dto.LoginUser;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.Invoices;
import com.compliance.documentinitiator.entity.UserCredentials;
import com.compliance.documentinitiator.service.InvoicesService;
import com.compliance.documentinitiator.service.UserCredentialsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/documentInitiator")
public class DocumentInitiatorController {

    @Autowired
    private InvoicesService invoicesService;

    @Autowired
    private UserCredentialsService userCredentialsService;

    @PostMapping("/userRegister")
    public ResponseEntity<String> registerUser(@RequestBody Users users) throws URISyntaxException {
        log.info("[Inside DocumentInitiatorController] Registering user");
        return userCredentialsService.registerUser(users);
    }

    @PostMapping("/validateUser")
    public ResponseEntity<UserCredentials> validateUser(@RequestBody LoginUser loginUser) {
        log.info("[Inside DocumentInitiatorController] validating user");
        return userCredentialsService.validateUser(loginUser);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<DocumentInitiatorResponse> validateInvoice(@RequestBody InitiatorPayload initiatorPayload) throws URISyntaxException {
        log.info("[Inside DocumentInitiatorController] validating invoice");
        return invoicesService.validateInvoice(initiatorPayload);
    }

    @GetMapping("/fetchAllInvoices")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Invoices>> getAllInvoices() {
        log.info("[Inside DocumentInitiatorController] fetching all invoices");
        return invoicesService.getAllInvoices();
    }
}
