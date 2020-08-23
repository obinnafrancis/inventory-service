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
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified"}, allowGetters = true)
public class User extends AbstractModel{
    @OneToOne(fetch = FetchType.EAGER)
    private Role role;
    @OneToOne(fetch = FetchType.EAGER)
    private Location location;
    @Column(unique = true, updatable = false)
    private String username;
    private String roleName;
    private String userType;
    @Column(unique = true, updatable = false)
    private String userTag;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, name = "created_on")
    private Date dateCreated;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "modified_on")
    private Date dateModified;
    private String firstname;
    private String middlename;
    private String lastname;
    private String gender;
    @Column(updatable = false, unique = true)
    private String email;
    @Column(unique = true)
    private String phoneNumber;
    @Column(nullable = false, length = 100)
    private String password;
}
