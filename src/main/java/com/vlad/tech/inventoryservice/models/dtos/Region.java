package com.vlad.tech.inventoryservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "regions", indexes = {@Index(name="Idx_country_region", columnList = "abbreviation,region,country")})
public class Region extends AbstractModel{
    @Column(nullable = false)
    private String abbreviation;
    @Column(nullable = false)
    private String region;
    @Column(nullable = false)
    private String country;
}
