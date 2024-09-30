package com.mealkings.order.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mealkings.order.entity.Cart;
import com.mealkings.order.entity.CartItem;
import com.mealkings.order.entity.Customer;
import com.mealkings.order.entity.Order;
import com.mealkings.order.entity.Restaurant;
import com.mealkings.order.repository.CartRepository;
import com.mealkings.order.repository.CustomerRepository;
import com.mealkings.order.repository.OrderRepository;
import com.mealkings.order.repository.RestaurantRepository;
import com.mealkings.order.exceptions.IDNotFoundException;

@Component
public class OrderOpsImpl implements OrderOps {
	
	@Autowired
	private OrderRepository orepo;	
	
	@Autowired
	private CustomerRepository crepo;	
	
	@Autowired
	private RestaurantRepository rrepo;	
	
	@Autowired
	private CartRepository cartrepo;	

	@Override
	public List<Order> getActiveOrderByRestaurant(long restaurant_id) {
		
		List<Order> resOrders = orepo.findByRestaurant_RestaurantId(restaurant_id);
		List<Order> activeOrders = new ArrayList<Order>();

		for(Order orders:resOrders)
			if(!(orders.getOrderStatus().equalsIgnoreCase("completed")))
				activeOrders.add(orders);
		
		return activeOrders;
	}

	@Override
	public List<Order> getPastOrderByRestaurant(long restaurant_id) {

		List<Order> resOrders = orepo.findByRestaurant_RestaurantId(restaurant_id);
		List<Order> pastOrders = new ArrayList<Order>();

		for(Order orders:resOrders)
			if(orders.getOrderStatus().equalsIgnoreCase("completed"))
				pastOrders.add(orders);
		
		return pastOrders;
	}

	@Override
	public void updateOrderStatus(long order_id, String status) throws IDNotFoundException {
		Order order = orepo.findById(order_id)
	            .orElseThrow(() -> new IDNotFoundException("Order either inactive or not present!"));
		order.setOrderStatus(status);

		orepo.save(order);
	}

	@Override
	public List<Order> getActiveOrderByUser(long user_id) {
		List<Order> userOrders = orepo.findByCustomer_CustomerId(user_id);
		List<Order> activeOrders = new ArrayList<Order>();

		for(Order orders:userOrders)
			if(!(orders.getOrderStatus().equalsIgnoreCase("completed")))
				activeOrders.add(orders);
		
		return activeOrders;
	}

	@Override
	public List<Order> getPastOrderByUser(long user_id) {
		List<Order> userOrders = orepo.findByRestaurant_RestaurantId(user_id);
		List<Order> pastOrders = new ArrayList<Order>();

		for(Order orders:userOrders)
			if(orders.getOrderStatus().equalsIgnoreCase("completed"))
				pastOrders.add(orders);
		
		return pastOrders;
	}

	@Override
	public void addOrder(long customer_id, long restaurant_id, long cart_id, Order order) throws IDNotFoundException {
		Customer customer = crepo.findById(customer_id).orElseThrow(() -> new IDNotFoundException("Customer with ID - "+customer_id+" does not exist"));
//		System.out.println(customer.getName());
		Restaurant restaurant = rrepo.findById(restaurant_id).orElseThrow(() -> new IDNotFoundException("Restaurant with ID - "+restaurant_id+" does not exist"));
		//System.out.println(restaurant.getName());
		Cart cart = cartrepo.findById(cart_id) .orElseThrow(() -> new IDNotFoundException("Cart with ID - "+restaurant_id+" does not exist"));
		//System.out.println(cart.getCart_id());
		
		double totalAmount = cart.getItems().stream()
			    .mapToDouble(CartItem::getTotalPrice)
			    .sum();
		
		if(order.isDiscountApplied())
			order.setTotalAmount(totalAmount*(1-0.01*order.getDiscountAmount()));
		else
			order.setTotalAmount(totalAmount);
		
		order.setOrderStatus("PLACED");
		order.setOrderDate(new Timestamp(System.currentTimeMillis()));
		order.setDeliverAddress(customer.getAddress());
		order.setCustomer(customer);
		order.setRestaurant(restaurant);
		
		orepo.save(order);
		
	}

	@Override
	public void cancelOrder(long order_id) throws IDNotFoundException {
		Order order = orepo.findById(order_id).orElseThrow(()-> new IDNotFoundException("Order with ID - "+order_id+" not found!"));
		order.setOrderStatus("CANCELLED");
		orepo.save(order);
	}

}