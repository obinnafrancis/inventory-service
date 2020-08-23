package com.vlad.tech.inventoryservice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    private long oemId;
    private String manufacturerNoName;
    private String manufacturerPartNumber;
    private String materialNumber;
    private String equipmentName;
    private String equipmentDescription;
    private double averagePrice;
    private String category;
}
