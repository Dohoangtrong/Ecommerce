package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginDto;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Objects;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

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
            HttpSession session) {

        if (result.hasErrors()) {
            return "login";
        }
        Customer customer = customerRepository.findByUsername(loginDto.getUsername());
        if (customer == null || !Objects.equals(loginDto.getPassword(), customer.getPassword())) {
            result.rejectValue("password", "error.loginDto", "Invalid username or password");
            return "login";
        }
        session.setAttribute("loggedInUser", customer);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
