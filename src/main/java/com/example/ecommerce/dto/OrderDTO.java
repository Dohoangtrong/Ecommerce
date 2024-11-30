package com.example.ecommerce.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDTO {
    private String paymentMethod;
    private String cardNumber;
    private String shippingAddress;
    private LocalDate deliveryDate;
    private String note;
    private Double totalPrice;
    private Long cartId;
    private String deliveryMethod;
}
