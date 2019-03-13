package com.example.login_consumer.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.login_consumer.feign.LoginFeign;
import com.example.login_consumer.utils.ResponseBean;

@RestController
public class LoginController {
	
	@Resource
	private LoginFeign loginFeign;
	
	@PostMapping("/login")
	public ResponseBean login(@RequestParam Map<String, String> userInfo) {
		String username = userInfo.get("username");
		String password = userInfo.get("password");
		return loginFeign.login(username, password);
	}
}
