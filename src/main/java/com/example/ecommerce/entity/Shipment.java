package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String shippingMethod;
    private String trackingNumber;
    private LocalDate estimatedDeliveryDate;

    public Shipment(String address, String shippingMethod, String trackingNumber) {
        this.address = address;
        this.shippingMethod = shippingMethod;
        this.trackingNumber = trackingNumber;
        this.estimatedDeliveryDate = getParsedDeliveryDate();
    }

    public Shipment() {
    }

    public LocalDate getParsedDeliveryDate() {
        LocalDate today = LocalDate.now();
        LocalDate deliveryDate = today.plusDays(4);
        return deliveryDate;
    }
}
