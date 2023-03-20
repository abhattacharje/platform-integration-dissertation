package com.compliance.documentinitiator.entity;

import com.compliance.documentinitiator.dto.Documents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invoices")
public class Invoices {

    @Id
    private String invoiceId;

    private String country;

    private String countryCode;

    private String sender;

    private Documents document;

    private String invoiceStatus;
}
