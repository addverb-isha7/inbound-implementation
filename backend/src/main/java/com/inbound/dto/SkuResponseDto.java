package com.inbound.dto;

import com.inbound.enums.SkuStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SkuResponseDto {

    private String skuId;
    private String skuName;
    private Double mrp;
    private String batchNumber;
    private LocalDate expiry;
    private Integer expectedQuantity;
    private Integer receivedQuantity;
    private SkuStatus status;
}