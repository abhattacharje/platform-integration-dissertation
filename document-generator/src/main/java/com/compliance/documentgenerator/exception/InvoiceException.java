package com.compliance.documentgenerator.exception;

import com.compliance.documentgenerator.dto.InvoiceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceException extends RuntimeException {

    private HttpStatusCode statusCode;
    private InvoiceResponse invoiceResponse;
}
