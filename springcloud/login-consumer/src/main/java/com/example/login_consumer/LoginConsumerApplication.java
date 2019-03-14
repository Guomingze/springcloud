package com.example.login_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableRedisHttpSession
public class LoginConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginConsumerApplication.class, args);
	}

}
