package com.mealkings.AppRegistry.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AppRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppRegistryApplication.class, args);
	}
}