package com.example.production.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.production.dao.PersonDao;
import com.example.production.pojo.Persion;
@Service
public class PersionServiceImpl implements PersionService {
	@Autowired
	private PersonDao persondao;
	@Override
	public List<Persion> all() {
		List<Persion> all = persondao.all();
		return all;
	}

	@Override
	public Persion selectAge(String name) {
		Persion selectAge = persondao.selectAge(name);
		if(selectAge==null) {
			return null;
		}
		return selectAge;
	}

	@Override
	public String addPersion(Persion persion) {
		int addPersion = persondao.addPersion(persion);
		if(addPersion==0) {
			return "增加失败" ;
		}
		System.out.println(persion.getName());
		return "增加成功";
	}

	@Override
	public String updatePersion(Persion persion) {
		int updatePersion = persondao.updatePersion(persion);
		if (updatePersion==0) {
			return "修改失败";
		}
		return "修改成功";
	}

	@Override
	public String delPersion(Integer id) {
		int delPersion = persondao.delPersion(id);
		if (delPersion==0) {
			return "删除失败";
		}
		return "删除成功";
	}

}
