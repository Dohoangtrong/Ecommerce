package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String name);

    Set<Item> findByCategoriesId(Long categoryId);

    Set<Item> findByNameContainingIgnoreCaseAndCategoriesId(String name, Long categoryId);

    List<Item> findByCategoriesNameContainingIgnoreCase(String categoryName);

    List<Item> findByNameContainingIgnoreCaseAndCategoriesNameContainingIgnoreCase(String name, String categoryName);
}
