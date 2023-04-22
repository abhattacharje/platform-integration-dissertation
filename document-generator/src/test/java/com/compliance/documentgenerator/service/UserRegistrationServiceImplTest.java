package com.compliance.documentgenerator.service;

import com.compliance.documentgenerator.dto.User;
import com.compliance.documentgenerator.entity.Users;
import com.compliance.documentgenerator.exception.DocumentGeneratorException;
import com.compliance.documentgenerator.repository.UsersRepository;
import com.compliance.documentgenerator.service.handler.UserRegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceImplTest {

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private User userPayload;

    @Mock
    private HttpClientErrorException httpClientErrorException;

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Test
    public void testRegisterUserPositiveFlow() {

        ResponseEntity<User> registerUserResponse = ResponseEntity.ok(userPayload);
        assertEquals(userRegistrationService.registerUser(userPayload).getStatusCode(), registerUserResponse.getStatusCode());
    }

    @Test
    public void testRegisterUserNegativeFlow() {

        Mockito.when(usersRepository.save(ArgumentMatchers.any(Users.class))).thenThrow(httpClientErrorException);
        DocumentGeneratorException thrown = assertThrows(
                DocumentGeneratorException.class,
                () -> userRegistrationService.registerUser(userPayload)
        );
    }
}
