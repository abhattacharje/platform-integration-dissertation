package com.compliance.documentgenerator.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "company-details")
public class CompanyDetails {

    private String userName;

    private String companyName;

    private String password;

    private String companyId;
}
