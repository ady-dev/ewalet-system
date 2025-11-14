package com.ewallet_system.ewallet_system.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TransactionResultDto {
    private String status;

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonProperty("new_balance")
    private BigDecimal newBalance;

    private String message;
}
