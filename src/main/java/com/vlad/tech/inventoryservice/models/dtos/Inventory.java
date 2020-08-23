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
@Table(name = "inventories")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified"}, allowGetters = true)
public class Inventory extends AbstractModel{
    @Column(unique = true, updatable = false, nullable = false)
    private String inventoryTag;
    @Column(unique = true, nullable = false)
    private String tagName;
    @OneToOne(fetch = FetchType.EAGER)
    private Project project;
    @OneToOne(fetch = FetchType.EAGER)
    private Product product;
    private String description;
    private String batchNumber;
    private long minimumQuantity;
    private long availableQuantity;
    private long usedQuantity;
    private long inventoryAccountBookValue;
    private double lastPurchasePrice;
    private double valueOfMaterialManaged;

    private String sparesCriticality;
    private String lastMovement;
    private String handlingUnit;
    private String specialStock;
    private String titleReceiptFlagName;
    private String reportingCategory;
    private String function;
    private String storageBinDescription;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, name = "created_on")
    private Date dateCreated;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "modified_on")
    private Date dateModified;
}
