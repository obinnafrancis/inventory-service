package com.vlad.tech.inventoryservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Builder
@Table(name = "oem")
@NoArgsConstructor
@AllArgsConstructor
public class Oem extends AbstractModel{
    @Column(unique = true, updatable = false, nullable = false)
    private String oemTag;
    @NotBlank
    @Column(unique = true, updatable = false, nullable = false)
    private String oemName;
    private boolean equipmentType;
    @NotBlank
    private String comment;
}
