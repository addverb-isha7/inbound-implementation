package com.inbound.dto;

import com.inbound.enums.InventoryOperation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class InventoryRequest {

    private InventoryOperation operation;
    private List<BatchRequest> items;
}