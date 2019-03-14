package com.example.login_consumer.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login_consumer.feign.LoginFeign;
import com.example.login_consumer.utils.ResponseBean;

@RestController
public class LoginController {

	@Resource
	private LoginFeign loginFeign;

	@PostMapping("/login")
	public ResponseBean login(@RequestBody Map<String, String> userInfo, HttpServletResponse httpServletResponse,
			HttpServletRequest httpServletRequest) {
		String username = userInfo.get("username");
		String password = userInfo.get("password");
		ResponseBean responseBean = loginFeign.login(username, password);
		Integer state = responseBean.getCode();
		
		//登录状态200为成功,成功将session放入cookie
		if(state.equals(200)) {
			Cookie cookie = new Cookie("JSESSIONID", responseBean.getData().toString());
			httpServletResponse.addCookie(cookie);
			responseBean.setData("登录成功");
		}
		return responseBean;
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest httpServletRequest) {
		System.out.println("------------------");
		System.out.println(httpServletRequest.getSession().getId());
		System.out.println("------------------");
		return loginFeign.test();
	}
}
