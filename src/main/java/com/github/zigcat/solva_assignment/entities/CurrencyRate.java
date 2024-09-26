package com.github.zigcat.solva_assignment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "currency_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {
    @Id
    @SequenceGenerator(name = "rate_sequence",
        sequenceName = "rate_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "rate_sequence")
    @Column(name = "rate_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_pair", nullable = false)
    private CurrencyPair currencyPair;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "close", precision = 10, scale = 2)
    private BigDecimal close;

    @Column(name = "previous_close", precision = 10, scale = 2)
    private BigDecimal previousClose;

    public CurrencyRate(CurrencyPair currencyPair, BigDecimal close, BigDecimal previousClose) {
        this.currencyPair = currencyPair;
        this.date = LocalDate.now();
        this.close = close;
        this.previousClose = previousClose;
    }
}
