package com.inbound.controller;

import com.inbound.dto.AsnResponseDto;
import com.inbound.dto.VerifyRequest;
import com.inbound.entity.Asn;
import com.inbound.service.AsnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.inbound.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.time.Instant;


@RestController
@RequestMapping("/api/asn")
@RequiredArgsConstructor
public class AsnController {

    private final AsnService asnService;

    // CREATE ASN
    @PostMapping
    public ResponseEntity<ApiResponse<Asn>> create(@RequestBody Asn asn) {

        Asn saved = asnService.createAsn(asn);

        return ResponseEntity.ok(
                ApiResponse.<Asn>builder()
                        .code("201")
                        .message("ASN Created Successfully")
                        .data(saved)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<AsnResponseDto>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<AsnResponseDto>>builder()
                        .code("200")
                        .message("ASN List Fetched Successfully")
                        .data(asnService.getAll())
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }

    // GET BY SHIPMENT
    @GetMapping("/{shipmentNumber}")
    public ResponseEntity<ApiResponse<AsnResponseDto>> getByShipment(
            @PathVariable String shipmentNumber) {

        return ResponseEntity.ok(
                ApiResponse.<AsnResponseDto>builder()
                        .code("200")
                        .message("ASN Fetched Successfully")
                        .data(asnService.getByShipment(shipmentNumber))
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }

    // VERIFY
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(
            @RequestBody VerifyRequest request) {

        String result = asnService.verify(request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code("200")
                        .message("Verification Completed")
                        .data(result)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }
}

