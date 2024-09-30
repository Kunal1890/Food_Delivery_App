package com.mealkings.authentication.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mealkings.authentication.entity.Credentials;
import com.mealkings.authentication.entity.Customer;
import com.mealkings.authentication.entity.Restaurant;
import com.mealkings.authentication.model.User;
import com.mealkings.authentication.repository.CustomerRepository;
import com.mealkings.authentication.repository.RestaurantRepository;
import com.mealkings.authentication.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    private boolean checkRole(String role) {
    	if(role.equalsIgnoreCase("CUSTOMER") || role.equalsIgnoreCase("RESTAURANT"))
    		return true;
    	return false;
    }
    
    private boolean checkUser(User user) {
    	List<Credentials> creds = userRepository.findAll();
    	
    	for(Credentials cred: creds)
    		if(cred.getEmail().equals(user.getEmail()) || cred.getUsername().equals(user.getUserName()))
    			return false;
    	return true;
    }

    @Override
    public void registerUser(User user) {
    	if(checkUser(user) && checkRole(user.getRole())) {
    		System.out.println(user);
    		Credentials newUser = new Credentials(user.getUserName(), user.getEmail(), passwordEncoder.encode(user.getPassword()), user.getRole());
    		userRepository.save(newUser);
    		
    		if(user.getRole().equalsIgnoreCase("customer"))
    		{
    			Customer cm = new Customer(user.getName(), user.getMobileNo(), user.getAddress(), user.getEmail(), newUser);
    			System.out.println(cm);
    			customerRepository.save(cm);
    		}
    		else
    		{
    			Restaurant ro = new Restaurant(user.getName(), user.getMobileNo(), user.getAddress(), (byte)0, true, newUser);
    			restaurantRepository.save(ro);
    		}
    	}
    }
}
