package com.mealkings.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealkings.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findByCustomer_CustomerId(long customerId);
	List<Order> findByRestaurant_RestaurantId(long restaurantId);
}