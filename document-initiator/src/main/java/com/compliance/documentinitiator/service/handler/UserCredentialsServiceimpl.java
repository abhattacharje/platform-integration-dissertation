package com.compliance.documentinitiator.service.handler;

import com.compliance.documentinitiator.configuration.PropertyConfiguration;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.UserCredentials;
import com.compliance.documentinitiator.exception.DocumentInitiatorException;
import com.compliance.documentinitiator.repository.UserCredentialsRepository;
import com.compliance.documentinitiator.service.UserCredentialsService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class UserCredentialsServiceimpl implements UserCredentialsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PropertyConfiguration propertyConfiguration;

    public ResponseEntity<String> registerUser(Users users) throws URISyntaxException {

        UserCredentials userCredentials = new UserCredentials();
        String userRegistrationApi = propertyConfiguration.getDocValidatorBaseUrl() + propertyConfiguration.getDocValidatorUserRegistrationApi();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(new ArrayList<>(Arrays.asList(MediaType.APPLICATION_JSON)));
        HttpEntity<Users> input = new HttpEntity<>(users, headers);
        ResponseEntity<String> userRegistrationResponse = null;

        try {
            log.info("Registering user");
            userCredentials.setClientId(users.getClientId());
            userCredentials.setClientSecret(passwordEncoder.encode(users.getClientSecret()));
            userCredentials.setCompanyId(users.getCompanyId());
            userCredentials.setRoles(users.getRoles());
            userCredentialsRepository.save(userCredentials);

            userRegistrationResponse = restTemplate.exchange(new URI(userRegistrationApi), HttpMethod.POST, input, String.class);
            log.info("User registered in Document Initiator as well as Document Validator DB");
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Exception occurred while registering user in the DB -> "+ex.getMessage());
            throw new DocumentInitiatorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(userRegistrationResponse.getBody());
    }
}
