package com.inbound.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InventoryData {

    private List<InventoryResult> results;
}