package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.login_production.utils.JedisUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginProductionApplicationTests {
	
	@Autowired
	private JedisUtil jedisUtil;

	@Test
	public void contextLoads() {
		jedisUtil.getJedis();
	}

}
