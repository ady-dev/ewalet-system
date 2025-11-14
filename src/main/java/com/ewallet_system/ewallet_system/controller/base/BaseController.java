package com.ewallet_system.ewallet_system.controller.base;

import org.springframework.http.ResponseEntity;

import com.ewallet_system.ewallet_system.dto.ResponseDto;
import com.ewallet_system.ewallet_system.enums.ResponseStatusCode;

public abstract class BaseController {
    protected <T> ResponseEntity<ResponseDto<T>> buildResponse(
            ResponseStatusCode statusCode,
            T data
    ) {
        return buildResponse(statusCode, data, "");
    }

    protected <T> ResponseEntity<ResponseDto<T>> buildResponse(
            ResponseStatusCode statusCode
    ) {
        return buildResponse(statusCode, null, "");
    }

    protected <T> ResponseEntity<ResponseDto<T>> buildResponse(T data) {
        return buildResponse(ResponseStatusCode.SUCCESS, data, "");
    }

    protected <T> ResponseEntity<ResponseDto<T>> buildResponse(
            ResponseStatusCode statusCode,
            T data,
            String customMessage
    ) {
        String message = (customMessage == null || customMessage.isBlank())
                ? statusCode.getDesc()
                : customMessage;

        ResponseDto<T> body = ResponseDto.<T>builder()
                .apiVersion("v1")
                .organization("E-Wallet")
                .code(statusCode.getCode())
                .title(statusCode.getTitle())
                .message(message)
                .data(data)
                .build();

        return ResponseEntity.status(statusCode.getHttpCode()).body(body);
    }

    protected <T> ResponseEntity<ResponseDto<T>> buildErrorResponse(
            ResponseStatusCode statusCode,
            String errorMessage,
            T data
    ) {
        return buildResponse(statusCode, data, errorMessage);
    }
}
