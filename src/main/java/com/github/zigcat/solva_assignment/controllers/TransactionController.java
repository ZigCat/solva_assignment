package com.github.zigcat.solva_assignment.controllers;

import com.github.zigcat.solva_assignment.entities.AppTransaction;
import com.github.zigcat.solva_assignment.entities.dto.TransactionDTO;
import com.github.zigcat.solva_assignment.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AppTransaction> create(@RequestBody TransactionDTO dto){
        try{
            AppTransaction transaction = service.saveTransaction(dto);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
