package com.vlad.tech.inventoryservice.models.requests;

import com.vlad.tech.inventoryservice.models.dtos.Product;
import com.vlad.tech.inventoryservice.models.dtos.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryRequest {
    private String tagName;
    private Project project;
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
}
