package com.vlad.tech.inventoryservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "storages")
@NoArgsConstructor
@AllArgsConstructor
public class Storage extends AbstractModel{
    @Column(unique = true, updatable = false, nullable = false)
    private String platformTag;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @OneToOne(fetch = FetchType.EAGER)
    private Location location;
    private long capacity;
}
