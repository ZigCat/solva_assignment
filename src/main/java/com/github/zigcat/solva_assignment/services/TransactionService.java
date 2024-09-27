package com.github.zigcat.solva_assignment.services;

import com.github.zigcat.solva_assignment.entities.AppLimit;
import com.github.zigcat.solva_assignment.entities.AppTransaction;
import com.github.zigcat.solva_assignment.entities.LimitTransaction;
import com.github.zigcat.solva_assignment.entities.dto.TransactionDTO;
import com.github.zigcat.solva_assignment.entities.dto.responses.TransactionExceededResponse;
import com.github.zigcat.solva_assignment.entities.enums.Currency;
import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import com.github.zigcat.solva_assignment.entities.enums.ExpenseCategory;
import com.github.zigcat.solva_assignment.repositories.LimitRepository;
import com.github.zigcat.solva_assignment.repositories.LimitTransactionRepository;
import com.github.zigcat.solva_assignment.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final LimitRepository limitRepository;
    private final CurrencyService currencyService;
    private final LimitTransactionRepository limitTransactionRepository;

    @Autowired
    public TransactionService(TransactionRepository repository,
                              LimitRepository limitRepository,
                              CurrencyService currencyService,
                              LimitTransactionRepository limitTransactionRepository) {
        this.repository = repository;
        this.limitRepository = limitRepository;
        this.currencyService = currencyService;
        this.limitTransactionRepository = limitTransactionRepository;
    }

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public LimitTransaction saveTransaction(TransactionDTO dto) throws IllegalArgumentException{
        AppTransaction transaction = new AppTransaction(dto.getAccountFrom(),
                dto.getAccountTo(),
                Currency.valueOf(dto.getCurrencyShortname()),
                BigDecimal.valueOf(Long.parseLong(dto.getSum())),
                ExpenseCategory.valueOf(dto.getExpenseCategory()));
        LimitTransaction limitTransaction = new LimitTransaction();
        checkIsLimitExceeded(transaction, limitTransaction);
        repository.save(transaction);
        limitTransaction.setTransaction(transaction);
        limitTransactionRepository.save(limitTransaction);
        return limitTransaction;
    }

    @Transactional
    public List<TransactionExceededResponse> getLimitExceeded(){
        AppLimit currentLimit = getLastLimit();
        List<LimitTransaction> limitTransactions = limitTransactionRepository.findAllByLimit(currentLimit);
        ArrayList<TransactionExceededResponse> response = new ArrayList<>();
        for(LimitTransaction l: limitTransactions){
            if(l.isLimitExceeded()){
                response.add(
                        new TransactionExceededResponse(
                                l.getTransaction(),
                                l.getLimit().getLimitSum().toString(),
                                l.getLimit().getLimitCurrencyShortname().toString(),
                                l.getLimit().getLimitDatetime().toString())
                        );
            }
        }
        return response;
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    private void checkIsLimitExceeded(AppTransaction transaction,
                                      LimitTransaction limitTransaction){
        AppLimit currentLimit = getLastLimit();
        limitTransaction.setLimit(currentLimit);
        List<AppTransaction> transactions = repository.findAll();
        BigDecimal sum = checkAndChangeToUsd(transaction);
        for(AppTransaction t: transactions){
            if(t.getDateTime().isAfter(currentLimit.getLimitDatetime())){
                sum = sum.add(checkAndChangeToUsd(t));
            }
        }
        if(sum.compareTo(currentLimit.getLimitSum()) > 0){
            limitTransaction.setLimitExceeded(true);
        } else {
            limitTransaction.setLimitExceeded(false);
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

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    private AppLimit getLastLimit(){
        List<AppLimit> limits = limitRepository.findAll(Sort.by(Sort.Direction.DESC, "limitDatetime"));
        return limits.get(0);
    }
}
