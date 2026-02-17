package com.inbound.repository;

import com.inbound.entity.Asn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AsnRepository extends JpaRepository<Asn, Long> {
    Optional<Asn> findByShipmentNumber(String shipmentNumber);
}

