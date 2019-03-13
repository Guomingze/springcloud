package com.example.login_production.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.login_production.domain.Userpermission;

@Repository
@Mapper
public interface UserpermissionDao extends MyMapper<Userpermission>{

}
