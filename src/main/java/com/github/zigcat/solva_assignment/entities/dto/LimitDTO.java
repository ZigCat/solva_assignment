package com.github.zigcat.solva_assignment.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitDTO {
    @JsonProperty("limit_sum")
    private String limitSum;
}
