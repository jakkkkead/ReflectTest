package com.web.dao;

import com.web.bean.Good;
import com.web.bean.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodDao {
    public int add(Good good);
    public List<Good> listGood(PageInfo pageInfo);
    public int getTotal();
    public List<Map<String,Object>> getDataByDate(@Param("start") String start, @Param("end")String end);
}
