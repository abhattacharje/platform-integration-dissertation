package com.compliance.documentinitiator.controller;

import com.compliance.documentinitiator.dto.DocumentInitiatorResponse;
import com.compliance.documentinitiator.dto.InitiatorPayload;
import com.compliance.documentinitiator.dto.LoginUser;
import com.compliance.documentinitiator.dto.Users;
import com.compliance.documentinitiator.entity.Invoices;
import com.compliance.documentinitiator.entity.UserCredentials;
import com.compliance.documentinitiator.service.InvoicesService;
import com.compliance.documentinitiator.service.UserCredentialsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DocumentInitiatorControllerTest {

    @InjectMocks
    private DocumentInitiatorController documentInitiatorController;

    @Mock
    private InvoicesService invoicesService;

    @Mock
    private UserCredentialsService userCredentialsService;

    @Mock
    private Users users;

    @Mock
    private LoginUser loginUser;

    @Mock
    private InitiatorPayload initiatorPayload;

    @Mock
    private UserCredentials userCredentials;

    @Mock
    private DocumentInitiatorResponse documentInitiatorResponse;

    @Mock
    private Invoices invoices;

    @Test
    public void testRegisterUser() throws URISyntaxException {

        ResponseEntity<String> registerUserResponse = ResponseEntity.ok("User Registered");
        Mockito.when(userCredentialsService.registerUser(users)).thenReturn(registerUserResponse);
        assertEquals(documentInitiatorController.registerUser(users).getStatusCode(), registerUserResponse.getStatusCode());
    }

    @Test
    public void testValidateUser() {

        ResponseEntity<UserCredentials> validateUserResponse = ResponseEntity.ok(userCredentials);
        Mockito.when(userCredentialsService.validateUser(loginUser)).thenReturn(validateUserResponse);
        assertEquals(documentInitiatorController.validateUser(loginUser).getStatusCode(), validateUserResponse.getStatusCode());
    }

    @Test
    public void testValidateInvoice() throws URISyntaxException {

        ResponseEntity<DocumentInitiatorResponse> validateInvoiceResponse = ResponseEntity.ok(documentInitiatorResponse);
        Mockito.when(invoicesService.validateInvoice(initiatorPayload)).thenReturn(validateInvoiceResponse);
        assertEquals(documentInitiatorController.validateInvoice(initiatorPayload).getStatusCode(), validateInvoiceResponse.getStatusCode());
    }

    @Test
    public void testGetAllInvoices() {

        ResponseEntity<List<Invoices>> getAllInvoicesResponse = ResponseEntity.ok(List.of(invoices));
        Mockito.when(invoicesService.getAllInvoices()).thenReturn(getAllInvoicesResponse);
        assertEquals(documentInitiatorController.getAllInvoices().getStatusCode(), getAllInvoicesResponse.getStatusCode());
    }
}
