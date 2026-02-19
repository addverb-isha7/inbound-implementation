package com.inbound.controller;

import com.inbound.dto.VerifyRequest;
import com.inbound.entity.Asn;
import com.inbound.service.AsnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asn")
@RequiredArgsConstructor
public class AsnController {

    private final AsnService asnService;

    @PostMapping
    public Asn create(@RequestBody Asn asn) {
        return asnService.createAsn(asn);
    }

    @GetMapping
    public List<Asn> getAll() {
        return asnService.getAll();
    }

    @GetMapping("/{shipmentNumber}")
    public Asn getDetails(@PathVariable String shipmentNumber) {
        return asnService.getByShipment(shipmentNumber);
    }

    @PostMapping("/verify")
    public String verify(@RequestBody VerifyRequest request) {
        return asnService.verify(request);
    }

}

