package com.compliance.documentgenerator.service;

import com.compliance.documentgenerator.dto.User;
import org.springframework.http.ResponseEntity;

public interface UserRegistrationService {

    ResponseEntity<User> registerUser(User userPayload);
}
