package com.example.login_production.service.impl;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.login_production.dao.PersionDao;
import com.example.login_production.service.LoginService;
import com.example.login_production.utils.ResponseBean;

@Service
public class LoginServiceImpl implements LoginService {
	
	
	@Autowired
	private PersionDao persionDao;

	@Override
	public ResponseBean login(String username, String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (Exception e) {
        	return new ResponseBean(400, "Login failure", "密码错误");
        }
        return new ResponseBean(200, "Login success", "登录成功");
	}

}
