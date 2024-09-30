package com.mealkings.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mealkings.authentication.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}