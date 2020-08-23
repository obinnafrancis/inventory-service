package com.vlad.tech.inventoryservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
public class Project extends AbstractModel{
    @Column(unique = true, updatable = false, nullable = false)
    private String assetTag;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "project_storages")
    private Set<Storage> storages;
}
