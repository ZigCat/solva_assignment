package com.github.zigcat.solva_assignment.entities.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.zigcat.solva_assignment.entities.AppTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionExceededResponse {
    @JsonProperty("transaction")
    private AppTransaction transaction;

    @JsonProperty("limit_sum")
    private String sum;

    @JsonProperty("limit_currency")
    private String currency;

    @JsonProperty("limit_timestamp")
    private String timestamp;
}
