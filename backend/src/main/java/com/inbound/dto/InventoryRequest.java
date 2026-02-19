package com.inbound.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class InventoryRequest {

    private String sku;
    private String batchNo;
    private Integer quantity;
    private Double mrp;
    private LocalDate expiryDate;
    private String status;
    private String operation;
}
