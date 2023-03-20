package com.compliance.documentgenerator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("roles")
    private String roles;
}
