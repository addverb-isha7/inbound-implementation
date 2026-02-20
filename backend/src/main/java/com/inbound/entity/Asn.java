package com.inbound.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "asn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long asnId;

    @Column(unique = true, nullable = false)
    private String shipmentNumber;

    private String supplier;

    @OneToMany(mappedBy = "asn", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Sku> skus;
}

