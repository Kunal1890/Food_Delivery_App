package com.mealkings.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CartItem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference(value = "cart-cartitems")
    private Cart cart; // Reference to the Cart

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @JsonBackReference(value = "item-cartitems")
    private Item item; // Reference to the Item

    private int quantity; // Quantity of the item in the cart
    private double totalPrice; // Total price for this quantity of the item

}