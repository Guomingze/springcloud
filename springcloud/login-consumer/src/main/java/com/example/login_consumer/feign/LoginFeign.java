package com.example.login_consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.login_consumer.utils.ResponseBean;


@FeignClient(value = "LOGIN-PRODUCTION")
public interface LoginFeign {

	/**
	 * 登录
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	 @RequestMapping(value = "/login", method = RequestMethod.POST)
	 ResponseBean login (@RequestParam String username, @RequestParam String password);
	 
	 /**
	  * 测试
	  * @return
	  */
	 @RequestMapping("/test")
	 String test(); 

}
