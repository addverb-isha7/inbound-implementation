package com.inbound.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 🔥 important
public class InventoryResponse {

    private String message;
    private Double mrp;
    private String operation;
    private String sku;
    private Boolean success;
}

