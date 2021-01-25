package com.me.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudFrontendApplication.class, args);
	}


}
