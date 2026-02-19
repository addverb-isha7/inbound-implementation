package com.inbound.client;

import com.inbound.dto.InventoryRequest;
import com.inbound.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InventoryClient {

    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryUrl;

    public InventoryResponse addToInventory(InventoryRequest request) {

        ResponseEntity<InventoryResponse> response =
                restTemplate.postForEntity(
                        inventoryUrl,
                        request,
                        InventoryResponse.class
                );
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());
        return response.getBody();
    }


}
