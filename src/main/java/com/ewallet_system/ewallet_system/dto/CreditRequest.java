package com.ewallet_system.ewallet_system.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreditRequest {
    private Long userId;
    private BigDecimal amount;
}
