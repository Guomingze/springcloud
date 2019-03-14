package com.example.login_production.controller;



import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.login_production.service.LoginService;
import com.example.login_production.utils.ResponseBean;


@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@PostMapping("/login")
	public ResponseBean login(@RequestParam String username, @RequestParam String password)
	{
		return loginService.login(username, password);
	}
	
	//需要admin角色
	@RequiresRoles("admin")
	//需要a的权限
	@RequiresPermissions(value = {"a", "b"}, logical=Logical.OR)
	@PostMapping("/test")
	public String test(HttpServletRequest httpServletRequest) {
		System.out.println("------------------");
		System.out.println(httpServletRequest.getSession().getId());
		System.out.println("------------------");
		return "欢迎访问";
	}
}
