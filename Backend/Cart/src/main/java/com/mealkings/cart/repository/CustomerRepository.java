package com.mealkings.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealkings.cart.entity.Cart;
import com.mealkings.cart.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}