package com.example.production.contorller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.production.pojo.Persion;
import com.example.production.service.PersionService;
@RequestMapping(value = "persion")
@RestController
public class TestController {
	@Autowired
	private PersionService persionService;
	@RequestMapping(value = "/all",method = RequestMethod.GET)
	public List<Persion> all() {
		List<Persion> all = persionService.all();
		return all;
	}
	@RequestMapping(value = "selectAge",method = RequestMethod.POST)
	public int selectAge(@RequestParam("name")String name) {
		Persion selectAge = persionService.selectAge(name);
		if(selectAge==null) {
			return -1;
		}
		return selectAge.getAge();
	}
	@RequestMapping(value = "addPersion",method = RequestMethod.POST)
	public String addPersion(@RequestBody Persion persion) {
		String addPersion = persionService.addPersion(persion);
		return addPersion;
	}
	@RequestMapping(value = "updatePersion",method = RequestMethod.POST)
	public String updatePersion(@RequestBody Persion persion) {
		String updatePersion = persionService.updatePersion(persion);
		return updatePersion;
	}
	@RequestMapping(value = "delPersion",method = RequestMethod.POST)
	public String delPersion(@RequestParam("id") Integer id) {
		String delPersion = persionService.delPersion(id);
		return delPersion;
	}
}
