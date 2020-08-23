package com.vlad.tech.inventoryservice.daos;

import com.vlad.tech.inventoryservice.models.dtos.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByName(String name);
//    @Query("SELECT p.name, p.description FROM role_permissions rp INNER JOIN permissions p ON rp.permission_id = p.id WHERE rp.role_id = :role_id")
//    public List<Permission> getPermissionByRoleId(@Param("role_id") long id);
}
