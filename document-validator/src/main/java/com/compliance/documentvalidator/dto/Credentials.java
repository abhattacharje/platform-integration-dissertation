package com.compliance.documentvalidator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Credentials {

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("clientSecret")
    private String clientSecret;

    @JsonProperty("companyId")
    private String companyId;
}
