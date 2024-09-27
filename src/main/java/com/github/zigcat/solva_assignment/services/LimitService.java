package com.github.zigcat.solva_assignment.services;

import com.github.zigcat.solva_assignment.entities.AppLimit;
import com.github.zigcat.solva_assignment.entities.dto.LimitDTO;
import com.github.zigcat.solva_assignment.repositories.LimitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class LimitService {
    private final LimitRepository repository;

    @Autowired
    public LimitService(LimitRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateLimits(){
        checkAndUpdateLimits();
    }

    @Transactional
    public AppLimit saveLimit(LimitDTO dto){
        AppLimit limit = new AppLimit(
                BigDecimal.valueOf(Double.parseDouble(dto.getLimitSum())));
        repository.save(limit);
        return limit;
    }

    @Transactional(readOnly = true)
    public List<AppLimit> getAll(){
        return repository.findAll();
    }

    @Transactional
    private void checkAndUpdateLimits(){
        List<AppLimit> limits = repository.findAll();
        for(AppLimit l: limits){
            if(l.getLimitDatetime().plusMonths(1).isBefore(ZonedDateTime.now())){
                l.setLimitDatetime(ZonedDateTime.now());
                repository.save(l);
            }
        }
    }
}
