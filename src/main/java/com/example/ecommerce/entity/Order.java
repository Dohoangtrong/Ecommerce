package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipment")
    private Shipment shipment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "cart")
    private Cart cart;

}

