package com.github.zigcat.solva_assignment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zigcat.solva_assignment.entities.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity(name = "app_limit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppLimit {
    @Id
    @SequenceGenerator(name = "limit_sequence",
            sequenceName = "limit_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "limit_sequence")
    private Integer id;

    @Column(name = "limit_sum", precision = 10, scale = 2)
    private BigDecimal limitSum;

    @Column(name = "limit_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC+6")
    private ZonedDateTime limitDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "limit_currency_shortname")
    private Currency limitCurrencyShortname;

    public AppLimit(BigDecimal limitSum,
                    Currency limitCurrencyShortname) {
        this.limitSum = limitSum;
        this.limitDatetime = ZonedDateTime.now();
        this.limitCurrencyShortname = limitCurrencyShortname;
    }
}
