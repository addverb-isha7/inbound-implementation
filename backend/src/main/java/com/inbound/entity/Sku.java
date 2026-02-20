package com.inbound.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inbound.enums.SkuStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "sku")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sku {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skuId;
    private String skuName;
    private Double mrp;
    private String batchNumber;
    private LocalDate expiry;
    private Integer expectedQuantity;
    private Integer receivedQuantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkuStatus status;

    @ManyToOne
    @JoinColumn(name = "asn_id")
    @JsonBackReference
    private Asn asn;
// THIS RUNS AUTOMATICALLY BEFORE INSERT
    @PrePersist
    public void prePersist() {

        if (receivedQuantity == null) {
            receivedQuantity = 0;
        }

        if (status == null) {
            status = SkuStatus.PENDING;
        }
    }
}

