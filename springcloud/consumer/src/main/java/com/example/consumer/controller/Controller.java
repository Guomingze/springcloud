package com.example.consumer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.consumer.pojo.Persion;
import com.example.consumer.test.Test;
@RequestMapping(value = "/persion")
@RestController
public class Controller {
	@Autowired
	private Test test;
	@RequestMapping(value = "/all",method = RequestMethod.GET)
	List<Persion> all(){
		return test.all();
	}
	
	@RequestMapping(value = "/selectAge",method = RequestMethod.POST)
	public int selectAge(String name) {
		return test.selectAge(name);
	}
	
	@RequestMapping(value = "/addPersion",method = RequestMethod.POST)
	public String addPersion(Persion persion) {
		System.out.println(persion.getName());
		return test.addPersion(persion);
	}
	
	@RequestMapping(value = "/updatePersion",method = RequestMethod.POST)
	public String updatePersion(Persion persion) {
		return test.updatePersion(persion);
	}
	
	@RequestMapping(value = "delPersion",method = RequestMethod.POST)
	public String delPersion(Integer id) {
		return test.delPersion(id);
	}
}
