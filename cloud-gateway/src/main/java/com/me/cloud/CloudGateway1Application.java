package com.me.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudGateway1Application {

	public static void main(String[] args) {
		SpringApplication.run(CloudGateway1Application.class, args);
	}

}
