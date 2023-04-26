package com.compliance.documentinitiator.service;

import com.compliance.documentinitiator.configuration.PropertyConfiguration;
import com.compliance.documentinitiator.dto.LoginUser;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.UserCredentials;
import com.compliance.documentinitiator.exception.DocumentInitiatorException;
import com.compliance.documentinitiator.exception.UserLoginException;
import com.compliance.documentinitiator.repository.UserCredentialsRepository;
import com.compliance.documentinitiator.service.handler.UserCredentialsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserCredentialsServiceImplTest {

    @InjectMocks
    private UserCredentialsServiceImpl userCredentialsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PropertyConfiguration propertyConfiguration;

    @Mock
    private UserCredentials userCredentials;

    @Mock
    private Users users;

    @Mock
    private HttpClientErrorException httpClientErrorException;

    @Mock
    private HttpServerErrorException httpServerErrorException;

    @Mock
    private LoginUser loginUser;

    @Mock
    private UserLoginException userLoginException;

    @Test
    public void testRegisterUserPositiveFlow() throws URISyntaxException {

        ResponseEntity<String> registerUserResponse = ResponseEntity.ok("User Registered");
        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(String.class))).thenReturn(registerUserResponse);
        assertEquals(userCredentialsService.registerUser(users).getStatusCode(), registerUserResponse.getStatusCode());
    }

    @Test
    public void testRegisterUserNegativeFlow() throws URISyntaxException {

        Mockito.lenient().when(restTemplate.exchange(Mockito.any(URI.class), Mockito.any(HttpMethod.class), Mockito.any(HttpEntity.class), eq(String.class))).thenThrow(httpClientErrorException);
        DocumentInitiatorException thrown = assertThrows(
                DocumentInitiatorException.class,
                () -> userCredentialsService.registerUser(users)
        );
    }

    @Test
    public void testValidateUserNegativeFlowIncorrectCredentials() {

        String clientSecret = "123";
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedClientSecret = encoder.encodeToString(clientSecret.getBytes());
        loginUser.setClientSecret(clientSecret);
        userCredentials.setClientSecret(encodedClientSecret);
        Optional<UserCredentials> userPrincipal = Optional.of(userCredentials);
        Mockito.lenient().when(userCredentialsRepository.findById(loginUser.getClientId())).thenReturn(userPrincipal);
        Mockito.lenient().when(loginUser.getClientSecret()).thenReturn(clientSecret);
        Mockito.lenient().when(userCredentials.getClientSecret()).thenReturn(encodedClientSecret);
        assertFalse(passwordEncoder.matches(clientSecret, encodedClientSecret));
        DocumentInitiatorException thrown = assertThrows(
                DocumentInitiatorException.class,
                () -> userCredentialsService.validateUser(loginUser)
        );
    }

    @Test
    public void testValidateUserNegativeFlowUserNotFound() {

        String clientSecret = "123";
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedClientSecret = encoder.encodeToString(clientSecret.getBytes());
        loginUser.setClientSecret(clientSecret);
        userCredentials.setClientSecret(encodedClientSecret);
        Optional<UserCredentials> userPrincipal = Optional.of(userCredentials);
        Mockito.lenient().when(!userPrincipal.isPresent()).thenThrow(userLoginException);
        DocumentInitiatorException thrown = assertThrows(
                DocumentInitiatorException.class,
                () -> userCredentialsService.validateUser(loginUser)
        );
    }
}
