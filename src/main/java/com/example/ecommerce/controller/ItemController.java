package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/list")
    public String getAllItemsForView(Model model) {
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "home";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        return item != null ? new ResponseEntity<>(item, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = itemService.createItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        Item updatedItem = itemService.updateItem(id, itemDetails);
        return updatedItem != null ? new ResponseEntity<>(updatedItem, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean deleted = itemService.deleteItem(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/search")
    public String searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryId,
            Model model) {
        if (name != null && !name.isEmpty()) {
            if ( categoryId == null || categoryId.isEmpty() ){
                List<Item> items = itemService.findItemsByName(name);
                Set<Category> categories = itemService.findCategoriesByItems(items);
                model.addAttribute("categories", categories);
                model.addAttribute("items", items);
            }else{
                Set<Item> items = itemService.searchItems(name, Long.parseLong(categoryId));
                model.addAttribute("items", items);
            }
        }else{
            if ( categoryId != null && !categoryId.isEmpty() ){
                Set<Item> items = itemService.getItemsByCategory(Long.parseLong(categoryId));
                model.addAttribute("items", items);
            }else{
                return "redirect:/";
            }
        }
        return "items";
    }

}
