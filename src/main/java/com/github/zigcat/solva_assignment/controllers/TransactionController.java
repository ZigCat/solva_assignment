package com.github.zigcat.solva_assignment.controllers;

import com.github.zigcat.solva_assignment.entities.AppTransaction;
import com.github.zigcat.solva_assignment.entities.LimitTransaction;
import com.github.zigcat.solva_assignment.entities.dto.TransactionDTO;
import com.github.zigcat.solva_assignment.entities.dto.responses.TransactionExceededResponse;
import com.github.zigcat.solva_assignment.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LimitTransaction> create(@RequestBody TransactionDTO dto){
        try{
            LimitTransaction limitTransaction = service.saveTransaction(dto);
            return new ResponseEntity<>(limitTransaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<TransactionExceededResponse>> getLimitExceeded(){
        List<TransactionExceededResponse> limitTransactions = service.getLimitExceeded();
        return new ResponseEntity<>(limitTransactions, HttpStatus.OK);
    }
}
