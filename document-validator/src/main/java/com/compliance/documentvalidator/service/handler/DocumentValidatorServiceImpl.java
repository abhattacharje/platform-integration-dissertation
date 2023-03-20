package com.compliance.documentvalidator.service.handler;

import com.compliance.documentvalidator.configuration.PropertyConfiguration;
import com.compliance.documentvalidator.dto.DocGeneratorPayload;
import com.compliance.documentvalidator.dto.DocumentValidatorResponse;
import com.compliance.documentvalidator.dto.Users;
import com.compliance.documentvalidator.entity.UserCredentials;
import com.compliance.documentvalidator.exception.DocumentValidatorException;
import com.compliance.documentvalidator.repository.UserCredentialsRepository;
import com.compliance.documentvalidator.service.DocumentValidatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

@Service
public class DocumentValidatorServiceImpl implements DocumentValidatorService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private PropertyConfiguration propertyConfiguration;

    public ResponseEntity<DocumentValidatorResponse> validateInvoice(DocGeneratorPayload docGeneratorPayload) throws URISyntaxException {

        String docGeneratorApi = propertyConfiguration.getDocGeneratorUrl()+propertyConfiguration.getDocGeneratorApi();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(new ArrayList<>(Arrays.asList(MediaType.APPLICATION_JSON)));

        Base64.Encoder encoder = Base64.getEncoder();
        String encodedContent = encoder.encodeToString(docGeneratorPayload.getDocumnet().getContent().getBytes());
        docGeneratorPayload.getDocumnet().setContent(encodedContent);

        HttpEntity<DocGeneratorPayload> input = new HttpEntity<>(docGeneratorPayload, headers);
        ResponseEntity<DocumentValidatorResponse> docGeneratorResponse;

        try {
            docGeneratorResponse = restTemplate.exchange(new URI(docGeneratorApi), HttpMethod.POST, input, DocumentValidatorResponse.class);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new DocumentValidatorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(docGeneratorResponse.getBody());
    }

    public ResponseEntity<String> registerUser(Users users) {

        UserCredentials userCredentials = new UserCredentials();
        try {
            userCredentials.setClientId(users.getClientId());
            userCredentials.setClientSecret(passwordEncoder.encode(users.getClientSecret()));
            userCredentials.setCompanyId(users.getCompanyId());
            userCredentials.setRoles(users.getRoles());
            userCredentialsRepository.save(userCredentials);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new DocumentValidatorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok("User added successfully...!!!");
    }
}
