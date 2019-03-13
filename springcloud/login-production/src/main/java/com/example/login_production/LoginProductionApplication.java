package com.example.login_production;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class LoginProductionApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginProductionApplication.class, args);
	}

}
