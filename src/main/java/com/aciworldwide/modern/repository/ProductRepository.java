package com.aciworldwide.modern.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aciworldwide.modern.domain.Product;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockIsLessThanEqual(int reorder);
}
