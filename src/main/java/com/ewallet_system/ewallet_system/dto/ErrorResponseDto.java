package com.ewallet_system.ewallet_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String status = "error";  
    private String message;           
}
