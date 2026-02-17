package com.inbound.repository;

import com.inbound.entity.Asn;
import com.inbound.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkuRepository extends JpaRepository<Sku, Long> {

    Optional<Sku> findBySkuIdAndAsn_ShipmentNumber(String skuId, String shipmentNumber);
}


