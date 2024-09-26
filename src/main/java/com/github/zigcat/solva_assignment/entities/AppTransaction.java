package com.github.zigcat.solva_assignment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.zigcat.solva_assignment.entities.enums.Currency;
import com.github.zigcat.solva_assignment.entities.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity(name = "app_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppTransaction {
    @Id
    @SequenceGenerator(name="transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence")
    private Integer id;

    @Column(name = "account_from", columnDefinition = "NUMERIC", nullable = false)
    private BigInteger accountFrom;

    @Column(name = "account_to", columnDefinition = "NUMERIC", nullable = false)
    private BigInteger accountTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_shortname")
    private Currency currencyShortname;

    @Column(precision = 10, scale = 2)
    private BigDecimal sum;

    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category")
    private ExpenseCategory expenseCategory;

    @Column(name = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC+6")
    private ZonedDateTime dateTime;

    public AppTransaction(BigInteger accountFrom,
                          BigInteger accountTo,
                          Currency currencyShortname,
                          BigDecimal sum,
                          ExpenseCategory expenseCategory) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.currencyShortname = currencyShortname;
        this.sum = sum;
        this.expenseCategory = expenseCategory;
        this.dateTime = ZonedDateTime.now();
    }
}
