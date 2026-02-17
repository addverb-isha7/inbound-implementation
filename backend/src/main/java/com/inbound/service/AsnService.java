package com.inbound.service;

import com.inbound.client.InventoryClient;
import com.inbound.dto.InventoryRequest;
import com.inbound.dto.VerifyRequest;
import com.inbound.entity.Asn;
import com.inbound.entity.Sku;
import com.inbound.repository.AsnRepository;
import com.inbound.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AsnService {

    private final AsnRepository asnRepository;
    private final SkuRepository skuRepository;
    private final InventoryClient inventoryClient;

    public Asn createAsn(Asn asn) {

        asn.getSkus().forEach(sku -> {
            sku.setStatus("PENDING");
            //sku.setReceivedQuantity(0);
            sku.setAsn(asn);
        });

        return asnRepository.save(asn);
    }

    public List<Asn> getAll() {
        return asnRepository.findAll();
    }

    public Asn getByShipment(String shipmentNumber) {
        return asnRepository.findByShipmentNumber(shipmentNumber)
                .orElseThrow(() -> new RuntimeException("ASN not found"));
    }

    public String verify(String shipmentNumber, VerifyRequest request) {

        Asn asn = getByShipment(shipmentNumber);

        Sku sku = skuRepository
                .findBySkuIdAndAsn_ShipmentNumber(request.getSkuId(), shipmentNumber)
                .orElseThrow(() -> new RuntimeException("SKU not found in this ASN"));

        boolean isSkuNameMatch = Objects.equals(sku.getSkuName(), request.getSkuName());
        boolean isBatchMatch = Objects.equals(sku.getBatchNumber(), request.getBatchNumber());
        boolean isExpiryMatch = Objects.equals(sku.getExpiry(), request.getExpiry());

        boolean isMrpMatch =
                sku.getMrp() != null &&
                        request.getMrp() != null &&
                        Math.abs(sku.getMrp() - request.getMrp()) < 0.01;

        if (isSkuNameMatch && isMrpMatch && isBatchMatch && isExpiryMatch) {
            sku.setStatus("GOOD");
        } else {
            sku.setStatus("BAD");
        }


        skuRepository.save(sku);

        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .sku(sku.getSkuId())                        // "SKU1"
                .batchNo(sku.getBatchNumber())              // "B1"
                .quantity(
                        sku.getExpectedQuantity() != null
                                ? sku.getExpectedQuantity()
                                : 100                               // fallback safe value
                )
                .mrp(
                        sku.getMrp() != null
                                ? sku.getMrp()
                                : 100.0
                )
                .expiryDate(sku.getExpiry())                // 2026-12-01
                .status(sku.getStatus())                    // GOOD / BAD
                .build();

        try {
            inventoryClient.addToInventory(inventoryRequest);
        } catch (Exception e) {
            System.out.println("Inventory service failed: " + e.getMessage());
        }
// Call inventory service
// inventoryClient.addToInventory(request);
        return "Verification Completed. Status: " + sku.getStatus();
    }

}

