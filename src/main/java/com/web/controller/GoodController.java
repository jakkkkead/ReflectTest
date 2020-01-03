package com.web.controller;

import com.web.bean.Good;
import com.web.bean.PageInfo;
import com.web.bean.RestResponse;
import com.web.bean.SearchRequestBody;
import com.web.service.GoodDataSynService;
import com.web.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/good")
public class GoodController {
    @Autowired
    private GoodService goodService;
    @Autowired
    private GoodDataSynService goodDataSynService;
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
    public RestResponse searchGood(SearchRequestBody params) throws IOException {
        PageInfo pageInfo = goodService.searchByNameOrMark(params);
        if (pageInfo == null){
            return new RestResponse().error("没有结果");
        }
        return new RestResponse().success(pageInfo);
    }
    @PostMapping("/getData")
    @ResponseBody
    public RestResponse getData(@RequestBody Map<String,String> params)  {
//        Map<String ,String > params = new HashMap<>(4);
//        BeanUtils.copyProperties(params,goodRequest);
        goodDataSynService.getJDGoodData(params.get("url"),params);
        return new RestResponse().success();
    }
    @GetMapping("/getType")
    public String getType(ModelAndView modelAndView){
        Map<String, Map<String, String>> typeList = goodService.getTypeList();
        modelAndView.addObject("typeList",typeList);
        return "/index";
    }
}
