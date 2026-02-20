package com.inbound.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResult {

    private String batchNo;
    private Integer previousQty;
    private Integer updatedQty;
    private String status;
}