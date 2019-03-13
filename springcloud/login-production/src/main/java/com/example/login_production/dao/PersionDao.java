package com.example.login_production.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.login_production.domain.Persion;


@Repository
@Mapper
public interface PersionDao extends MyMapper<Persion>{
}
