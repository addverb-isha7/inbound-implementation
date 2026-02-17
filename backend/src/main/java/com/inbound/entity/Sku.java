package com.inbound.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String skuId;          // supplier entered
    private String skuName;
    private Double mrp;
    private String batchNumber;
    private LocalDate expiry;
    private Integer expectedQuantity;

    private String status;         // PENDING / GOOD / BAD

    @ManyToOne
    @JoinColumn(name = "asn_id")
    @JsonIgnore
    private Asn asn;

}

