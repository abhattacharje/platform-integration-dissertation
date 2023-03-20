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
public class InitiatorPayload {

    @JsonProperty("country")
    private String country;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("document")
    private Documents document;

    @JsonProperty("users")
    private Users users;
}
