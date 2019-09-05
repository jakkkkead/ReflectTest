package com.web.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface Test {
    Integer getVersion(@Param("id") int id);
}
