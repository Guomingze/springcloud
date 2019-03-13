package com.example.production.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.production.pojo.Persion;

@Repository
@Mapper
public interface PersonDao {
	//查看表中全部信息
	List<Persion> all();
	//根据姓名查年龄
	Persion selectAge(String name);
	//增加人员信息
	int addPersion(Persion persion);
	//修改人员信息
	int updatePersion(Persion persion);
	//删除人员信息
	int delPersion(Integer id);
}
