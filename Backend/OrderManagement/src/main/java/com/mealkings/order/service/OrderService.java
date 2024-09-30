package com.mealkings.order.service;

import java.util.List;

import com.mealkings.order.entity.Order;
import com.mealkings.order.exceptions.IDNotFoundException;

public interface OrderService {
	
	public void addOrder(long customer_id, long restaurant_id, long cart_id, Order order) throws IDNotFoundException;
	
	public List<Order> getActiveOrderByRestaurant(int restaurant_id);
	
	public List<Order> getPastOrderByRestaurant(int restaurant_id);
	
	public List<Order> getActiveOrderByUser(long user_id);
	
	public List<Order> getPastOrderByUser(int user_id);
	
	public void updateOrderStatus(int order_id, String status) throws IDNotFoundException;
	
	public void cancelOrder(long order_id) throws IDNotFoundException;
}
