package com.github.zigcat.solva_assignment.repositories;

import com.github.zigcat.solva_assignment.entities.CurrencyRate;
import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {
    Optional<CurrencyRate> findByCurrencyPair(CurrencyPair currencyPair);
}
