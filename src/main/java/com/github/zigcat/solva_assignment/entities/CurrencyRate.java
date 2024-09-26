package com.github.zigcat.solva_assignment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {
    @Id
    @SequenceGenerator(name = "pair_sequence",
        sequenceName = "pair_sequence",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "pair_sequence")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_pair")
    private CurrencyPair currencyPair;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(precision = 10, scale = 2)
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
