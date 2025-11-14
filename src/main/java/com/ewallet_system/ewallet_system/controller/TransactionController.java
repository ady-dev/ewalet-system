package com.ewallet_system.ewallet_system.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ewallet_system.ewallet_system.controller.base.BaseController;
import com.ewallet_system.ewallet_system.dto.CreditRequest;
import com.ewallet_system.ewallet_system.dto.DebitRequest;
import com.ewallet_system.ewallet_system.dto.ErrorResponseDto;
import com.ewallet_system.ewallet_system.dto.ResponseDto;
import com.ewallet_system.ewallet_system.dto.TransactionResultDto;
import com.ewallet_system.ewallet_system.enums.ResponseStatusCode;
import com.ewallet_system.ewallet_system.service.EWalletSystem;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController extends BaseController {
    private final EWalletSystem eWalletSystem;

    @PostMapping("/credit")
    public ResponseEntity<ResponseDto<TransactionResultDto>> credit(@RequestBody CreditRequest request) {
        TransactionResultDto result = eWalletSystem.credit(request);
        return buildResponse(ResponseStatusCode.SUCCESS, result);
    }

    @PostMapping("/debit")
    public ResponseEntity<ResponseDto<TransactionResultDto>> debit(@RequestBody DebitRequest request) {
        TransactionResultDto result = eWalletSystem.debit(request);
        return buildResponse(ResponseStatusCode.SUCCESS, result);
    }
}
