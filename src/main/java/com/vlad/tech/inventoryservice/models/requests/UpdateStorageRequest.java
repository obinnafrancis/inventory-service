package com.vlad.tech.inventoryservice.models.requests;

import com.vlad.tech.inventoryservice.models.dtos.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStorageRequest {
    private String name;
    private String description;
    private long locationId;
    private long capacity;
}
