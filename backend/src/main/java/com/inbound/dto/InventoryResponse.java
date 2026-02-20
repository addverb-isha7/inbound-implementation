package com.inbound.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponse {

    private Boolean success;
    private String message;
    private InventoryData data;
}