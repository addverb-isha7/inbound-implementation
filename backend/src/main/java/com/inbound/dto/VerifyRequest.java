package com.inbound.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VerifyRequest {

private String shipmentNumber;
    private String skuId;
    private String skuName;
    private String batchNumber;
    private Double mrp;
    private LocalDate expiry;
 //   private Integer receivedQuantity;
}
