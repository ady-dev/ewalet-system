package com.ewallet_system.ewallet_system.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DebitRequest {
    private Long userId;
    private BigDecimal amount;
}
