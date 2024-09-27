package com.github.zigcat.solva_assignment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "limit_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitTransaction {
    @Id
    @SequenceGenerator(name = "limit_transaction_sequence",
            sequenceName = "limit_transaction_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "limit_transaction_sequence")
    @Column(name = "limit_transaction_id")
    private Long id;

    @Column(name = "limit_exceeded", nullable = false)
    private boolean limitExceeded;

    @ManyToOne
    @JoinColumn(name = "limit_id")
    private AppLimit limit;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private AppTransaction transaction;

    public LimitTransaction(AppLimit limit,
                            AppTransaction transaction,
                            boolean limitExceeded) {
        this.limit = limit;
        this.transaction = transaction;
        this.limitExceeded = limitExceeded;
    }
}
