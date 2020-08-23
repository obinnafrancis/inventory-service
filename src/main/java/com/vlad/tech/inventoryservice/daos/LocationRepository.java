package com.vlad.tech.inventoryservice.daos;

import com.vlad.tech.inventoryservice.models.dtos.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByLocationTagAndRegionId(String locationTag, long regionId);
    Location findByLocationTag(String locationTag);
}
