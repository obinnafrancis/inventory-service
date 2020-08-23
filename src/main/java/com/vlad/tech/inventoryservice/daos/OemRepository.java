package com.vlad.tech.inventoryservice.daos;

import com.vlad.tech.inventoryservice.models.dtos.Oem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OemRepository extends JpaRepository<Oem, Long> {
    Oem findByOemName(String oemName);
}
