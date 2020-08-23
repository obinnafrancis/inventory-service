package com.vlad.tech.inventoryservice.daos;

import com.vlad.tech.inventoryservice.models.dtos.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByTagName(String tagName);
}
