package com.web.dao;

import com.web.bean.Good;
import com.web.bean.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodDao {
     int add(Good good);
     List<Good> listGood(PageInfo pageInfo);
     int getTotal();
     List<Map<String,Object>> getDataByDate(@Param("start") String start, @Param("end")String end);
     int batchInsert(@Param("list")List<Good> list);
     List<Map<String,String>> getTypeList();
}
