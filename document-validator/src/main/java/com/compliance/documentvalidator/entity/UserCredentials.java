package com.compliance.documentvalidator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user-credentials")
public class UserCredentials {

    @Id
    private String clientId;

    private String clientSecret;

    private String companyId;

    private String roles;
}
