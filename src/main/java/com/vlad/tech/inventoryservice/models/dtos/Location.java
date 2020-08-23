package com.vlad.tech.inventoryservice.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified"}, allowGetters = true)
public class Location extends AbstractModel{
    @Column(unique = true, updatable = false, nullable = false)
    private String locationTag;
    @NotBlank
    private String companyTag;
    @NotBlank
    private String locationDescription;
    @OneToOne(fetch = FetchType.EAGER)
    private Region region;
    @NotBlank
    private String climate;
    @NotBlank
    private String environment;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, name = "created_on")
    private Date dateCreated;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, name = "modified_on")
    private Date dateModified;
}
