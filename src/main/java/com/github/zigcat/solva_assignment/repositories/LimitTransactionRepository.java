package com.github.zigcat.solva_assignment.repositories;

import com.github.zigcat.solva_assignment.entities.AppLimit;
import com.github.zigcat.solva_assignment.entities.LimitTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitTransactionRepository extends JpaRepository<LimitTransaction, Long> {
    List<LimitTransaction> findAllByLimit(AppLimit limit);
}
