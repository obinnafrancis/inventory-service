package com.vlad.tech.inventoryservice.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified"}, allowGetters = true)
public class Product extends AbstractModel{
    @Column(unique = true, updatable = false, nullable = false)
    private String productTag;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    private Oem oem;
    @NotBlank
    private String manufacturerNoName;
    @NotBlank
    private String manufacturerPartNumber;
    @NotBlank
    private String materialNumber;
    private String equipmentName;
    private String equipmentDescription;
    private double averagePrice;
    private String category;
}
