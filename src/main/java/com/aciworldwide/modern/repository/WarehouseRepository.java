package com.aciworldwide.modern.repository;

import com.aciworldwide.modern.domain.Warehouse;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Warehouse entity.
 */
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {

    @Query("select distinct warehouse from Warehouse warehouse left join fetch warehouse.products")
    List<Warehouse> findAllWithEagerRelationships();

    @Query("select warehouse from Warehouse warehouse left join fetch warehouse.products where warehouse.id =:id")
    Warehouse findOneWithEagerRelationships(@Param("id") Long id);

}
