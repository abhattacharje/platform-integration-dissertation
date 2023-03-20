package com.compliance.documentinitiator.service;

import com.compliance.documentinitiator.dto.Users;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface UserCredentialsService {

    ResponseEntity<String> registerUser(Users users) throws URISyntaxException;
}
