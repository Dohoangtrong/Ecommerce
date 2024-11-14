package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private int ratingValue;


    public Rating() {}

    public Rating(Item item, Customer customer, int ratingValue, String review) {
        this.item = item;
        this.customer = customer;
        this.ratingValue = ratingValue;
    }
}
