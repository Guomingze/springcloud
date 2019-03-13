package com.example.consumer.test;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.consumer.pojo.Persion;
@Service
public class TestImpl implements Test{

	@Override
	public List<Persion> all() {
		System.out.println("熔断了");
		return null;
	}

	@Override
	public String selectPwd(String username) {
		return "服务暂不可用";
	}

	@Override
	public String addPersion(Persion persion) {
		return "服务暂不可用";
	}

	@Override
	public String updatePersion(Persion persion) {
		return "服务暂不可用";
	}

	@Override
	public String delPersion(Integer id) {
		return "服务暂不可用";
	}

}
