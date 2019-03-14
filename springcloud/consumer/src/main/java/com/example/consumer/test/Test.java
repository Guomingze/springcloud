package com.example.consumer.test;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.consumer.pojo.Persion;
@FeignClient(value = "test-production",fallback =TestImpl.class)
public interface Test {
	@RequestMapping(value = "/persion/all",method = RequestMethod.GET)
	List<Persion> all();
	@RequestMapping(value = "/persion/selectPwd",method = RequestMethod.POST)
	public String selectPwd(@RequestParam("username") String username);
	@RequestMapping(value = "/persion/addPersion",method = RequestMethod.POST)
	public String addPersion(Persion persion);
	@RequestMapping(value = "/persion/updatePersion",method = RequestMethod.POST)
	public String updatePersion(Persion persion);
	@RequestMapping(value = "/persion/delPersion",method = RequestMethod.POST)
	public String delPersion(@RequestParam("id")Integer id);
}
