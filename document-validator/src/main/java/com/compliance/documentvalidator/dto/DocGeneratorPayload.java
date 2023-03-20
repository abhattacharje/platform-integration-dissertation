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
public class DocGeneratorPayload {

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("document")
    private Document documnet;
}
