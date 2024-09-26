package com.github.zigcat.solva_assignment.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    @JsonProperty("account_from")
    private String accountFrom;

    @JsonProperty("account_to")
    private String accountTo;

    @JsonProperty("currency_shortname")
    private String currencyShortname;

    @JsonProperty("sum")
    private String sum;

    @JsonProperty("expense_category")
    private String expenseCategory;
}
