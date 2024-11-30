package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/register")
    public String showRegisterForm(Customer customer) {
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid Customer customer, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        if (customerRepository.findByUsername(customer.getUsername()) != null) {
            result.rejectValue("username", "error.customer", "Username already exists");
            return "register";
        }

        customerRepository.save(customer);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("loginDto") LoginDto loginDto,
            BindingResult result,
            HttpSession session,
            Model model) {

        if (result.hasErrors()) {
            return "login";
        }
        Customer customer = customerRepository.findByUsername(loginDto.getUsername());
        if (customer == null || !Objects.equals(loginDto.getPassword(), customer.getPassword())) {
            result.rejectValue("password", "error.loginDto", "Invalid username or password");
            model.addAttribute("loginError", "Invalid username or password");

            return "login";
        }
        session.setAttribute("loggedInUser", customer);

        List<CartItem> tempCartItems = (List<CartItem>) session.getAttribute("cart");
        if (tempCartItems != null && !tempCartItems.isEmpty()) {

            Cart userCart = cartRepository.findByCustomerId(customer.getId());
            if (userCart == null) {
                userCart = new Cart();
                userCart.setCustomer(customer);
                userCart = cartRepository.save(userCart);
            }

            for (CartItem tempItem : tempCartItems) {
                CartItem existingItem = cartItemRepository.findByCartAndItem(userCart, tempItem.getItem());
                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + tempItem.getQuantity());
                    cartItemRepository.save(existingItem);
                } else {
                    CartItem newCartItem = new CartItem(userCart, tempItem.getItem(), tempItem.getQuantity());
                    cartItemRepository.save(newCartItem);
                }
            }
            session.removeAttribute("cart");
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
