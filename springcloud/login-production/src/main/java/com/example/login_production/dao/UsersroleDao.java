package com.example.login_production.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.login_production.domain.Usersrole;

@Repository
@Mapper
public interface UsersroleDao extends MyMapper<Usersrole>{

}
