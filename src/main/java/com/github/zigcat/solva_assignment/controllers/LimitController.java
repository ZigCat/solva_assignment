package com.github.zigcat.solva_assignment.controllers;

import com.github.zigcat.solva_assignment.entities.AppLimit;
import com.github.zigcat.solva_assignment.entities.dto.LimitDTO;
import com.github.zigcat.solva_assignment.services.LimitService;
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
@RequestMapping("/api/limit")
public class LimitController {
    private final LimitService service;

    @Autowired
    public LimitController(LimitService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AppLimit>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppLimit> create(@RequestBody LimitDTO dto){
        AppLimit limit = service.saveLimit(dto);
        return new ResponseEntity<>(limit, HttpStatus.CREATED);
    }
}
