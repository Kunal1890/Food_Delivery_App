package com.mealkings.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealkings.order.entity.Order;
import com.mealkings.order.exceptions.IDNotFoundException;
import com.mealkings.order.model.NewOrderParams;
import com.mealkings.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService service;
	
	@PostMapping("/add")
	public ResponseEntity<String> addOrder(@RequestBody NewOrderParams neworder){
		try {
			System.out.println(neworder);
			service.addOrder(neworder.getCustomer_id(), neworder.getRestaurant_id(), neworder.getCart_id(), neworder.getNeworder());
		} catch (IDNotFoundException e) {
			return ResponseEntity.ok(e.getMessage());
		}
		return ResponseEntity.ok("Order placed!");
	}
	
	@PutMapping("{order_id}/cancel")
	public ResponseEntity<String> addOrder(@PathVariable long order_id){
		try {
			service.cancelOrder(order_id);
		} catch (IDNotFoundException e) {
			return ResponseEntity.ok(e.getMessage());
		}
		return ResponseEntity.ok("Order cancelled!");
	}
	
	@GetMapping("/restaurant/{id}/active")
	public ResponseEntity<List<Order>> getActiveOrderByRestaurant(@PathVariable int id){
		return ResponseEntity.ok(service.getActiveOrderByRestaurant(id));
	}
	
	@GetMapping("/user/{id}/active")
	public ResponseEntity<List<Order>> getActiveOrderByUser(@PathVariable long id){
		return ResponseEntity.ok(service.getActiveOrderByUser(id));
	}
	
	@GetMapping("/restaurant/{id}/past")
	public ResponseEntity<List<Order>> getPastOrderByRestaurant(@PathVariable int id){
		return ResponseEntity.ok(service.getPastOrderByRestaurant(id));
	}
	
	@GetMapping("/user/{id}/past")
	public ResponseEntity<List<Order>> getPastOrderByUser(@PathVariable int id){
		return ResponseEntity.ok(service.getPastOrderByUser(id));
	}
	
	@PostMapping("{order_id}/updatestatus")
	public ResponseEntity<String> addOrderHistory(@PathVariable int order_id, @RequestBody String status) {
		try {
			service.updateOrderStatus(order_id, status);
			return ResponseEntity.status(HttpStatus.OK).body("Updated status!");
		} catch (IDNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}