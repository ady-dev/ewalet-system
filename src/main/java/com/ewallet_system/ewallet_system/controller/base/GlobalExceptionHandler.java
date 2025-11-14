package com.ewallet_system.ewallet_system.controller.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ewallet_system.ewallet_system.dto.ResponseDto;
import com.ewallet_system.ewallet_system.dto.TransactionResultDto;
import com.ewallet_system.ewallet_system.execption.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController{
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseDto<TransactionResultDto>> handleAppException(AppException ex) {

        TransactionResultDto dto = TransactionResultDto.builder()
                .status("error")
                .message(ex.getMessage())
                .transactionId(null)
                .newBalance(null)
                .build();

        return buildErrorResponse(
                ex.getStatusCode(),
                ex.getMessage(),
                dto
        );
    }
}
