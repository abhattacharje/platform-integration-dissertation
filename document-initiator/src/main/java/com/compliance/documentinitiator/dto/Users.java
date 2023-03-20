package com.compliance.documentinitiator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Users {

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("clientSecret")
    private String clientSecret;

    @JsonProperty("companyId")
    private String companyId;

    @JsonProperty("roles")
    private String roles;
}
