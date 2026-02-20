package com.inbound.dto;

import com.inbound.enums.SkuStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class BatchRequest {

    private String sku;
    private String batchNo;
    private Integer quantity;
    private Double mrp;
    private LocalDate expiryDate;
    private SkuStatus status;
}