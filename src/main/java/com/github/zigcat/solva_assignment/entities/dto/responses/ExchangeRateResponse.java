package com.github.zigcat.solva_assignment.entities.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("rate")
    private String rate;

    @JsonProperty("timestamp")
    private String timestamp;
}
