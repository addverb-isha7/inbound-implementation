package com.inbound.service;

import com.inbound.client.InventoryClient;
import com.inbound.dto.InventoryRequest;
import com.inbound.dto.InventoryResponse;
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

    public String verify(VerifyRequest request) {

        Asn asn = getByShipment(request.getShipmentNumber());

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

