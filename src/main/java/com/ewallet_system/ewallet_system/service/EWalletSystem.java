package com.ewallet_system.ewallet_system.service;

import com.ewallet_system.ewallet_system.dto.CreditRequest;
import com.ewallet_system.ewallet_system.dto.DebitRequest;
import com.ewallet_system.ewallet_system.dto.TransactionResultDto;

public interface EWalletSystem {
    TransactionResultDto credit(CreditRequest request);
    TransactionResultDto debit(DebitRequest request);
}
