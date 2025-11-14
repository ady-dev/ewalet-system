package com.ewallet_system.ewallet_system.execption;

import com.ewallet_system.ewallet_system.enums.ResponseStatusCode;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ResponseStatusCode statusCode;

    public AppException(String message) {
        super(message);
        this.statusCode = ResponseStatusCode.E_GENERIC_ERROR;
    }

    public AppException(ResponseStatusCode statusCode) {
        super(statusCode.getDesc());
        this.statusCode = statusCode;
    }

    public AppException(ResponseStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    
}
