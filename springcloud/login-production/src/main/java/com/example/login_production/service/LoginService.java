package com.example.login_production.service;

import javax.servlet.http.HttpServletResponse;

import com.example.login_production.utils.ResponseBean;

public interface LoginService {

	ResponseBean login(String usernaString,String password);
}
