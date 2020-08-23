package com.vlad.tech.inventoryservice.daos;

import com.vlad.tech.inventoryservice.models.dtos.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
