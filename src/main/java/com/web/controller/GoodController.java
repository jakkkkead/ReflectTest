package com.web.controller;

import com.web.bean.Good;
import com.web.bean.PageInfo;
import com.web.bean.RestResponse;
import com.web.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/good")
public class GoodController {
    @Autowired
    private GoodService goodService;
    @PostMapping("/add")
    @ResponseBody
    public RestResponse goodAdd(Good good){
        int i = goodService.add(good);
        if (i > 0){
            return new RestResponse().success();
        }else {
            return new RestResponse().error("添加失败");
        }
    }
    @GetMapping("/list")
    @ResponseBody
    public Object listGood(PageInfo pageInfo){
        List<Good> list = goodService.listGoods(pageInfo);
        int total = goodService.getTotal();
        Map<String,Object> map = new HashMap<>(2);
        map.put("total",total);
        map.put("rows",list);
        return map;
    }
    @PostMapping("/search")
    @ResponseBody
    public RestResponse searchGood(String content) throws IOException {
        List<Map<String, Object>> goods = goodService.searchByNameOrMark(content);
        if (CollectionUtils.isEmpty(goods)){
            return new RestResponse().error("没有结果");
        }
        return new RestResponse().success(goods);
    }
}
