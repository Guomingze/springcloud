package com.example.production.service;

import java.util.List;

import com.example.production.pojo.Persion;

public interface PersionService {
	//查看表中全部信息
		List<Persion> all();
		//根据姓名查年龄
		Persion selectAge(String name);
		//增加人员信息
		String addPersion(Persion persion);
		//修改人员信息
		String updatePersion(Persion persion);
		//删除人员信息
		String delPersion(Integer id);
}
