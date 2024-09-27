package com.github.zigcat.solva_assignment.services;

import com.github.zigcat.solva_assignment.entities.AppLimit;
import com.github.zigcat.solva_assignment.entities.AppTransaction;
import com.github.zigcat.solva_assignment.entities.dto.TransactionDTO;
import com.github.zigcat.solva_assignment.entities.enums.Currency;
import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import com.github.zigcat.solva_assignment.entities.enums.ExpenseCategory;
import com.github.zigcat.solva_assignment.repositories.CurrencyRateRepository;
import com.github.zigcat.solva_assignment.repositories.LimitRepository;
import com.github.zigcat.solva_assignment.repositories.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final LimitRepository limitRepository;
    private final CurrencyService currencyService;

    @Autowired
    public TransactionService(TransactionRepository repository,
                              LimitRepository limitRepository,
                              CurrencyService currencyService) {
        this.repository = repository;
        this.limitRepository = limitRepository;
        this.currencyService = currencyService;
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public AppTransaction saveTransaction(TransactionDTO dto) throws IllegalArgumentException{
        AppTransaction transaction = new AppTransaction(dto.getAccountFrom(),
                dto.getAccountTo(),
                Currency.valueOf(dto.getCurrencyShortname()),
                BigDecimal.valueOf(Long.parseLong(dto.getSum())),
                ExpenseCategory.valueOf(dto.getExpenseCategory()));
        checkIsLimitExceeded(transaction);
        repository.save(transaction);
        return transaction;
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    private void checkIsLimitExceeded(AppTransaction transaction){
        List<AppLimit> limits = limitRepository.findAll(Sort.by(Sort.Direction.DESC, "limitDatetime"));
        AppLimit currentLimit = limits.get(0);
        List<AppTransaction> transactions = repository.findAll();
        BigDecimal sum = checkAndChangeToUsd(transaction);
        for(AppTransaction t: transactions){
            if(t.getDateTime().isAfter(currentLimit.getLimitDatetime())){
                sum = sum.add(checkAndChangeToUsd(t));
            }
        }
        if(sum.compareTo(currentLimit.getLimitSum()) > 0){
            transaction.setLimitExceeded(true);
        } else {
            transaction.setLimitExceeded(false);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    private BigDecimal checkAndChangeToUsd(AppTransaction transaction){
        if(transaction.getCurrencyShortname().equals(Currency.USD)){
            return transaction.getSum();
        } else {
            return currencyService.calculateExpenses
                    (transaction.getSum(), CurrencyPair.valueOf
                            (transaction.getCurrencyShortname()+"_USD"));
        }
    }
}
