package com.github.zigcat.solva_assignment.controllers;

import com.github.zigcat.solva_assignment.entities.enums.CurrencyPair;
import com.github.zigcat.solva_assignment.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/currency")
@Slf4j
public class CurrencyController {
    private final CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping("/expense")
    public ResponseEntity<BigDecimal> calculateExpenses(@RequestParam BigDecimal amount,
                                                        @RequestParam String currencyPair){
        try{
            CurrencyPair pair = CurrencyPair.valueOf(currencyPair);
            BigDecimal expense = service.calculateExpenses(amount, pair);
            log.info(expense.toEngineeringString());
            return new ResponseEntity<>(expense, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
