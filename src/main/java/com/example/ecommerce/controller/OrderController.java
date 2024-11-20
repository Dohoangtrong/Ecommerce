package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/")
    public String getAllOrders(Model model, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            model.addAttribute("cartItems", cartItems);

            double totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                    .sum();
            model.addAttribute("totalPrice", totalPrice);

            List<Order> orders = orderService.getAllOrders();
            model.addAttribute("orders", orders);

            model.addAttribute("customer", loggedInUser);
        }else{
            return "redirect:/login";
        }
        return "order";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            model.addAttribute("order", order);
            return "orderDetail";
        } else {
            return "redirect:/orders";
        }
    }

    @PostMapping
    public String createOrder(@ModelAttribute Order order) {
        orderService.createOrder(order);
        return "redirect:/orders";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(@PathVariable Long id, @ModelAttribute Order orderDetails) {
        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        if (updatedOrder != null) {
            return "redirect:/orders";
        } else {
            return "redirect:/orders";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}

