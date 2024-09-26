package com.github.zigcat.solva_assignment.repositories;

import com.github.zigcat.solva_assignment.entities.AppTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<AppTransaction, Integer> {
}
