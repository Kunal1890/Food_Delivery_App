package com.mealkings.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mealkings.order.entity.Order;
import com.mealkings.order.exceptions.IDNotFoundException;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderOps oservice;	

	@Override
	public List<Order> getActiveOrderByRestaurant(int restaurant_id) {
		return oservice.getActiveOrderByRestaurant(restaurant_id);
	}

	@Override
	public List<Order> getPastOrderByRestaurant(int restaurant_id) {
		return oservice.getPastOrderByRestaurant(restaurant_id);
	}
	
	@Override
	public List<Order> getActiveOrderByUser(long user_id) {
		return oservice.getActiveOrderByUser(user_id);
	}
	
	@Override
	public List<Order> getPastOrderByUser(int user_id) {
		return oservice.getPastOrderByUser(user_id);
	}

	@Override
	public void updateOrderStatus(int order_id, String status) throws IDNotFoundException {
		oservice.updateOrderStatus(order_id, status);
	}

	@Override
	public void addOrder(long customer_id, long restaurant_id, long cart_id, Order order) throws IDNotFoundException {
		
		oservice.addOrder(customer_id, restaurant_id, cart_id, order);
	}

	@Override
	public void cancelOrder(long order_id) throws IDNotFoundException {
		oservice.cancelOrder(order_id);
	}

}