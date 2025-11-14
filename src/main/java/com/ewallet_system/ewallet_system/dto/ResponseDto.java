package com.ewallet_system.ewallet_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private String apiVersion;
    private String organization;
    private String code;
    private String title;
    private String message;
    private T data;
    // pagination
    private Integer pageSize;
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalRecords;
}
