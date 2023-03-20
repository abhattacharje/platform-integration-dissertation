package com.compliance.documentvalidator.controller;

import com.compliance.documentvalidator.dto.*;
import com.compliance.documentvalidator.service.DocumentValidatorService;
import com.compliance.documentvalidator.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/docValidator")
public class DocumentValidatorController {

    @Autowired
    private DocumentValidatorService documentValidatorService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/userRegister")
    public ResponseEntity<String> registerUser(@RequestBody Users users) {
        log.info("[Inside DocumentValidatorController] registering user");
        return documentValidatorService.registerUser(users);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<DocumentValidatorResponse> validateInvoice(@RequestBody InitiatorPayload initiatorPayload) throws URISyntaxException {

        DocGeneratorPayload docGeneratorPayload = new DocGeneratorPayload();

        log.info("Preparing the doc generator payload");
        docGeneratorPayload.setSender(initiatorPayload.getSender());
        docGeneratorPayload.setDocumnet(initiatorPayload.getDocument());
        log.info("Doc generator payload prepared");

        return documentValidatorService.validateInvoice(docGeneratorPayload);
    }

    @PostMapping("/getToken")
    public String authorizeAndGetToken(@RequestBody Credentials credentials) {
        log.info("Authenticating user and generating JWT");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getClientId(), credentials.getClientSecret()));
        if (authentication.isAuthenticated()) {
            log.info("User authenticated and JWT generated");
            return jwtService.generateToken(credentials.getClientId());
        } else {
            log.error("User not found, cannot generate JWT");
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
