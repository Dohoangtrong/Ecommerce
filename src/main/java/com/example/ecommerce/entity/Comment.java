package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @Column(nullable = false)
    private String content;

    public Comment() {}

    public Comment(Item item, Customer customer, String content) {
        this.item = item;
        this.customer = customer;
        this.content = content;
    }
}