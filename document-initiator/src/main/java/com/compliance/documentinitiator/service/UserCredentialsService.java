package com.compliance.documentinitiator.service;

import com.compliance.documentinitiator.dto.LoginUser;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.UserCredentials;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;

public interface UserCredentialsService {

    ResponseEntity<String> registerUser(Users users) throws URISyntaxException;

    ResponseEntity<UserCredentials> validateUser(LoginUser loginUser);
}
