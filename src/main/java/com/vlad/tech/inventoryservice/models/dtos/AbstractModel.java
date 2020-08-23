package com.vlad.tech.inventoryservice.models.dtos;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
