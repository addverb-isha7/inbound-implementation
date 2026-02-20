package com.inbound.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AsnResponseDto {

    private String shipmentNumber;
    private String supplier;
    private List<SkuResponseDto> skus;
}