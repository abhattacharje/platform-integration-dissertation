package com.compliance.documentgenerator.service.handler;

import com.compliance.documentgenerator.dto.User;
import com.compliance.documentgenerator.entity.Users;
import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import com.compliance.documentgenerator.repository.UsersRepository;
import com.compliance.documentgenerator.service.UserRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public ResponseEntity<User> registerUser(User userPayload) {

        Users users = new Users();

        users.setUserName(userPayload.getUserName());
        users.setPassword(passwordEncoder.encode(userPayload.getPassword()));
        users.setRoles(userPayload.getRoles());

        try {
            usersRepository.save(users);
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new DocumentGeneratorException(ex.getStatusCode(), ex.getMessage());
        }
        return ResponseEntity.ok(userPayload);
    }
}
