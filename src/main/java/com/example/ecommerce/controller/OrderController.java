package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
            model.addAttribute("totalPrice", totalPrice);

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

    @GetMapping("/history")
    public String historyOrders(Model model, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            List<Order> orders = orderService.getOrdersByCartId(cart.getIdCart());
            model.addAttribute("orders", orders);
        }else{
            return "redirect:/login";
        }
        return "history";
    }

    public static String generateTrackingNumber() {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = dateFormat.format(new Date(timestamp));
        String trackingNumber = formattedDate + "0311";

        return trackingNumber;
    }



    @PostMapping("/createOrder")
    public String createOrder(@ModelAttribute OrderDTO orderDTO) {
        Order order = new Order();

        String trackingNumber = generateTrackingNumber();
        String deliveryMethod = orderDTO.getDeliveryMethod();

        order.setShipment(new Shipment(orderDTO.getShippingAddress(), deliveryMethod, trackingNumber));
        order.setPayment(new Payment( orderDTO.getTotalPrice(), orderDTO.getPaymentMethod(), "Success"));
        Long cartId = orderDTO.getCartId();

        Cart cart = cartRepository.findById(cartId).get();
        order.setCart(cart);

        cartItemRepository.deleteAll();

        orderService.createOrder(order);
        return "redirect:/";
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

