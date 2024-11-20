package com.example.ecommerce.service;

import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }


    public Item getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.orElse(null);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item itemDetails) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setName(itemDetails.getName());
            item.setPrice(itemDetails.getPrice());
            return itemRepository.save(item);
        } else {
            return null;
        }
    }

    public boolean deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);

        if (item.isPresent()) {
            itemRepository.delete(item.get());
            return true;
        } else {
            return false;
        }
    }

    public List<Item> findItemsByName(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Item> findItemsByCategory(String categoryName) {
        return itemRepository.findByCategoriesNameContainingIgnoreCase(categoryName);
    }

    public List<Item> findItemsByNameAndCategory(String name, String categoryName) {
        return itemRepository.findByNameContainingIgnoreCaseAndCategoriesNameContainingIgnoreCase(name, categoryName);
    }

    public Set<Item> getItemsByCategory(Long categoryId) {
        return itemRepository.findByCategoriesId(categoryId);
    }

    public Set<Item> searchItems(String name, Long categoryId) {
        return itemRepository.findByNameContainingIgnoreCaseAndCategoriesId(name, categoryId);
    }

    public Set<Category> findCategoriesByItems(List<Item> items) {
        Set<Category> categories = new HashSet<>();
        for (Item item : items) {
            categories.addAll(item.getCategories());
        }
        return categories;
    }

}
