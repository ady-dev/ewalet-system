package com.ewallet_system.ewallet_system.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ewallet_system.ewallet_system.dto.CreditRequest;
import com.ewallet_system.ewallet_system.dto.DebitRequest;
import com.ewallet_system.ewallet_system.dto.TransactionResultDto;
import com.ewallet_system.ewallet_system.enums.ResponseStatusCode;
import com.ewallet_system.ewallet_system.execption.AppException;
import com.ewallet_system.ewallet_system.model.Transaction;
import com.ewallet_system.ewallet_system.model.User;
import com.ewallet_system.ewallet_system.repository.TransactionRepository;
import com.ewallet_system.ewallet_system.repository.UserRepository;
import com.ewallet_system.ewallet_system.service.EWalletSystem;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EWalletSystemImpl implements EWalletSystem {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResultDto credit(CreditRequest request) {

        BigDecimal amount = validateAmount(request.getAmount());
        User user = getUserWithLock(request.getUserId());

        BigDecimal oldBalance = user.getBalance();
        BigDecimal newBalance = oldBalance.add(amount);
        user.setBalance(newBalance);

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setType(Transaction.Type.CREDIT);

        transactionRepository.save(tx);

        log.info("CREDIT userId={} amount={} oldBalance={} newBalance={} txId={}",
                user.getId(), amount, oldBalance, newBalance, tx.getId());

        return TransactionResultDto.builder()
                .status("success")
                .transactionId(tx.getId())
                .newBalance(newBalance)
                .message(null)
                .build();
    }

    @Override
    @Transactional
    public TransactionResultDto debit(DebitRequest request) {

        BigDecimal amount = validateAmount(request.getAmount());
        User user = getUserWithLock(request.getUserId());

        BigDecimal oldBalance = user.getBalance();
        if (oldBalance.compareTo(amount) < 0) {
            log.warn("DEBIT FAILED (insufficient funds) userId={} amount={} balance={}",
                    user.getId(), amount, oldBalance);

            throw new AppException(
                    ResponseStatusCode.ERROR_INSUFFICIENT_FUNDS,
                    "Insufficient funds"
            );
        }

        BigDecimal newBalance = oldBalance.subtract(amount);
        user.setBalance(newBalance);

        Transaction tx = new Transaction();
        tx.setUser(user);
        tx.setAmount(amount);
        tx.setType(Transaction.Type.DEBIT);

        transactionRepository.save(tx);

        log.info("DEBIT userId={} amount={} oldBalance={} newBalance={} txId={}",
                user.getId(), amount, oldBalance, newBalance, tx.getId());

        return TransactionResultDto.builder()
                .status("success")
                .transactionId(tx.getId())
                .newBalance(newBalance)
                .message(null)
                .build();
    }

    /**
     * Validate amount must > 0.
     */
    private BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ResponseStatusCode.ERROR_INVALID_AMOUNT, "Invalid amount");
        }
        return amount;
    }

    /**
     * Lock user row with PESSIMISTIC_WRITE.
     */
    private User getUserWithLock(Long userId) {
        return userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new AppException(
                        ResponseStatusCode.ERROR_USER_NOT_FOUND,
                        "User not found"
                ));
    }
}
