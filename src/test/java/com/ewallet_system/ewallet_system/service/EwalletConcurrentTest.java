package com.ewallet_system.ewallet_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ewallet_system.ewallet_system.dto.CreditRequest;
import com.ewallet_system.ewallet_system.dto.DebitRequest;
import com.ewallet_system.ewallet_system.execption.AppException;
import com.ewallet_system.ewallet_system.model.User;
import com.ewallet_system.ewallet_system.repository.UserRepository;

@SpringBootTest
class EwalletConcurrentTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EWalletSystem eWalletSystem;

    @Test
    void concurrentCreditShouldProduceCorrectFinalBalance() throws Exception {

        // Create user with initial balance 0
        User user = new User();
        user.setUsername("concurrent-user-" + UUID.randomUUID());
        user.setBalance(BigDecimal.ZERO);
        user = userRepository.save(user);

        Long userId = user.getId();

        final int threadCount = 100;
        final int txPerThread = 100;
        final BigDecimal amountPerTx = new BigDecimal("1000");

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < txPerThread; j++) {
                        CreditRequest req = new CreditRequest();
                        req.setUserId(userId);
                        req.setAmount(amountPerTx);

                        try {
                            eWalletSystem.credit(req);
                        } catch (Exception ex) {
                            // log, but do not fail concurrency test
                            ex.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // release all threads
        doneLatch.await();
        executor.shutdown();

        // Verify balance
        User reloaded = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        BigDecimal expected = amountPerTx.multiply(
                BigDecimal.valueOf((long) threadCount * txPerThread)
        );

        assertEquals(0, expected.compareTo(reloaded.getBalance()),
                "Final balance should match expected total");
    }

    @Test
    void concurrentDebitShouldBeSafe() throws Exception {

        User user = new User();
        user.setUsername("debit-user-" + UUID.randomUUID());
        user.setBalance(new BigDecimal("1000000"));
        user = userRepository.save(user);

        Long userId = user.getId();

        int threadCount = 100;
        BigDecimal amount = new BigDecimal("20000");

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    start.await();
                    DebitRequest req = new DebitRequest();
                    req.setUserId(userId);
                    req.setAmount(amount);

                    try {
                        eWalletSystem.debit(req);
                        successCount.incrementAndGet();
                    } catch (AppException ex) {
                        failCount.incrementAndGet();
                    }

                } catch (Exception ignored) {
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await();
        executor.shutdown();

        User reloaded = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Max allowed success
        int maxSuccess = 1000000 / 20000; // 50
        assertEquals(maxSuccess, successCount.get(), "Debit success count must match");
        assertEquals(0, reloaded.getBalance().compareTo(BigDecimal.ZERO), "Must Be 0");
        assertTrue(failCount.get() > 0, "There should be some failed debit due to insufficient funds");
    }
}
