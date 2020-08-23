package com.vlad.tech.inventoryservice.models.requests;


import com.vlad.tech.inventoryservice.models.dtos.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationRequest {
    private String locationTag;
    private String companyTag;
    private String locationDescription;
    private String country;
    private String climate;
    private String environment;
    private Region region;
}
