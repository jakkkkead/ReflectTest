package com.web.service;

import com.web.bean.Good;
import com.web.bean.PageInfo;
import com.web.bean.SearchRequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GoodService {
    public int add(Good good);
    public List<Good> listGoods(PageInfo pageInfo);
    public int getTotal();

    /**
     * 根据 商品名称和品牌查询
     * @param param
     * @return
     */
    public PageInfo searchByNameOrMark(SearchRequestBody param) throws IOException;
    public Map<String, Map<String,String>> getTypeList();
}
