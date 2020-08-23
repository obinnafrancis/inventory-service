package com.vlad.tech.inventoryservice.models.requests;

import com.vlad.tech.inventoryservice.models.dtos.Storage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequest {
    private String name;
    private Set<Storage> storages;
}
