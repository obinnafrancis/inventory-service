package com.vlad.tech.inventoryservice.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRegionRequest {
    private String abbreviation;
    private String region;
    private String country;
}
