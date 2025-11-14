package com.ewallet_system.ewallet_system.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.ewallet_system.ewallet_system.dto.CreditRequest;
import com.ewallet_system.ewallet_system.dto.DebitRequest;
import com.ewallet_system.ewallet_system.dto.ResponseDto;
import com.ewallet_system.ewallet_system.model.User;
import com.ewallet_system.ewallet_system.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserNotFound() {

        DebitRequest req = new DebitRequest();
        req.setUserId(1234567L);
        req.setAmount(new BigDecimal("1000"));

        ResponseEntity<ResponseDto> resp =
                restTemplate.postForEntity("/api/transactions/debit", req, ResponseDto.class);

        assertEquals(404, resp.getStatusCode().value());
        assertEquals("80004", resp.getBody().getCode());

        Map data = (Map) resp.getBody().getData();
        assertEquals("error", data.get("status"));
        assertEquals("User not found", data.get("message"));
    }

    @Test
    void testCreditInvalidAmount() {

        CreditRequest req = new CreditRequest();
        req.setUserId(1L);
        req.setAmount(BigDecimal.ZERO);

        ResponseEntity<ResponseDto> resp =
                restTemplate.postForEntity("/api/transactions/credit", req, ResponseDto.class);

        assertEquals(400, resp.getStatusCode().value());
        assertEquals("80004", resp.getBody().getCode());

        Map data = (Map) resp.getBody().getData();
        assertEquals("error", data.get("status"));
        assertEquals("Invalid amount", data.get("message"));
    }

    @Test
    void testDebitInsufficientFunds() {
        User user = new User();
        user.setUsername("test-user-" + UUID.randomUUID());
        user.setBalance(BigDecimal.ZERO);
        user = userRepository.save(user);

        Long userId = user.getId();

        DebitRequest req = new DebitRequest();
        req.setUserId(userId);
        req.setAmount(new BigDecimal("1000"));

        ResponseEntity<ResponseDto> resp =
                restTemplate.postForEntity("/api/transactions/debit", req, ResponseDto.class);

        assertEquals(400, resp.getStatusCode().value());
        assertEquals("80005", resp.getBody().getCode());

        Map data = (Map) resp.getBody().getData();
        assertEquals("error", data.get("status"));
        assertEquals("Insufficient funds", data.get("message"));
    }
}
