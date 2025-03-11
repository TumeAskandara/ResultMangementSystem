package com.example.resultmanagementsystem.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseDTO {
    private MetaDTO meta;
    private Object data;
    private Object error;
    private PaginationDTO paginationDto;
    private String correlationId;
    private String transactionId;
}