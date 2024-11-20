package com.example.ecommerce.controller;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartRepository cartRepository;


    @GetMapping("/")
    public String showHomePage(Model model, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);

        List<Category> categories = categoryService.getAllCategories();

        int cartItemCount = 0;

        if (loggedInUser != null) {
            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            if (cart != null) {
                cartItemCount = cart.getCartItems().size();
            }
            model.addAttribute("user", loggedInUser);
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart != null) {
                cartItemCount = cart.size();
            }
        }
        model.addAttribute("cartItemCount", cartItemCount);
        model.addAttribute("categories", categories);

        return loggedInUser != null ? "home" : "homepage";
    }


}