package com.vlad.tech.inventoryservice.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "permissions")
public class Permission extends AbstractModel{
    @NotBlank
    @Column(unique = true)
    private String name;
    private String description;
    public Permission (String name, String description){
        this.name = name;
        this.description = description;
    }
}
