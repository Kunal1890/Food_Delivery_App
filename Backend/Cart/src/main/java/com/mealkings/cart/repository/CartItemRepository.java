package com.mealkings.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealkings.cart.entity.Cart;
import com.mealkings.cart.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);  // Use Cart entity as parameter
    CartItem findByCartAndItem_itemId(Cart cart, Long itemId); // Use Cart entity as parameter
}