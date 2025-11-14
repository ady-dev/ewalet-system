package com.ewallet_system.ewallet_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ewallet_system.ewallet_system.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
