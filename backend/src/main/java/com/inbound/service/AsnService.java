package com.inbound.service;

import com.inbound.client.InventoryClient;
import com.inbound.dto.*;
import com.inbound.entity.Asn;
import com.inbound.entity.Sku;
import com.inbound.repository.AsnRepository;
import com.inbound.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AsnService {

    private final AsnRepository asnRepository;
    private final SkuRepository skuRepository;
    private final InventoryClient inventoryClient;

    private AsnResponseDto mapToDto(Asn asn) {

        List<SkuResponseDto> skuDtos = asn.getSkus()
                .stream()
                .map(sku -> SkuResponseDto.builder()
                        .skuId(sku.getSkuId())
                        .skuName(sku.getSkuName())
                        .mrp(sku.getMrp())
                        .batchNumber(sku.getBatchNumber())
                        .expiry(sku.getExpiry())
                        .expectedQuantity(sku.getExpectedQuantity())
                        .receivedQuantity(sku.getReceivedQuantity())
                        .status(sku.getStatus())
                        .build())
                .toList();

        return AsnResponseDto.builder()
                .shipmentNumber(asn.getShipmentNumber())
                .supplier(asn.getSupplier())
                .skus(skuDtos)
                .build();
    }

    public Asn createAsn(Asn asn) {

        List<Sku> validSkus = filterValidSkus(asn.getSkus());

        validSkus.forEach(sku -> {
            sku.setAsn(asn);  // Only set relationship
        });
        asn.setSkus(validSkus);

        return asnRepository.save(asn);
    }

    private List<Sku> filterValidSkus(List<Sku> skus) {

        Map<String, LocalDate> skuIdentityMap = new HashMap<>();
        List<Sku> validSkus = new ArrayList<>();

        for (Sku sku : skus) {

            String key = sku.getSkuId() + "_" +
                    sku.getMrp() + "_" +
                    sku.getBatchNumber();

            if (!skuIdentityMap.containsKey(key)) {

                // First time seeing this SKU identity
                skuIdentityMap.put(key, sku.getExpiry());
                validSkus.add(sku);

            } else {

                LocalDate existingExpiry = skuIdentityMap.get(key);

                if (Objects.equals(existingExpiry, sku.getExpiry())) {
                    // Same expiry → allowed
                    validSkus.add(sku);
                }

                // If expiry different → skip this SKU (reject silently)
            }
        }

        return validSkus;
    }

    public List<AsnResponseDto> getAll() {

        return asnRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public AsnResponseDto getByShipment(String shipmentNumber) {

        Asn asn = asnRepository.findByShipmentNumber(shipmentNumber)
                .orElseThrow(() -> new RuntimeException("ASN not found"));

        return mapToDto(asn);
    }

    public String verify(VerifyRequest request) {

        Asn asn = asnRepository.findByShipmentNumber(request.getShipmentNumber())
                .orElseThrow(() -> new RuntimeException("ASN not found"));

        Sku sku = skuRepository
                .findBySkuIdAndAsn_ShipmentNumber(
                        request.getSkuId(),
                        request.getShipmentNumber())
                .orElseThrow(() -> new RuntimeException("SKU not found"));

        // Step 1: Perform local verification
        boolean isSkuNameMatch = Objects.equals(sku.getSkuName(), request.getSkuName());
        boolean isBatchMatch = Objects.equals(sku.getBatchNumber(), request.getBatchNumber());
        boolean isExpiryMatch = Objects.equals(sku.getExpiry(), request.getExpiry());

        boolean isMrpMatch =
                sku.getMrp() != null &&
                        request.getMrp() != null &&
                        Math.abs(sku.getMrp() - request.getMrp()) < 0.01;

        String tentativeStatus =
                (isSkuNameMatch && isBatchMatch && isExpiryMatch && isMrpMatch)
                        ? "GOOD"
                        : "DAMAGED";

        // Step 2: Build inventory request
        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .sku(sku.getSkuId())
                .batchNo(sku.getBatchNumber())
                .quantity(sku.getExpectedQuantity())
                .mrp(sku.getMrp())
                .expiryDate(sku.getExpiry())
                .status(tentativeStatus)
                .operation("ADD")
                .build();

        try {
            InventoryResponse response = inventoryClient.addToInventory(inventoryRequest);

            if (response != null && Boolean.TRUE.equals(response.getSuccess())
                    && "Stock added successfully".equalsIgnoreCase(response.getMessage())) {

                sku.setStatus(tentativeStatus); // GOOD or DAMAGED

            } else {
                System.out.println(response);

                sku.setStatus("PENDING");
            }

        } catch (Exception e) {

            // If inventory service crashed / 500 / timeout
            sku.setStatus("PENDING");
        }

        skuRepository.save(sku);

        return "Final Status: " + sku.getStatus();
    }

}

