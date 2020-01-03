package com.web.controller;

import com.web.bean.RestResponse;
import com.web.service.GoodDataSynService;
import com.web.util.ElasticClientUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/es")
public class ElasticController {
    @Autowired
    private GoodDataSynService goodDataSynService;
    @GetMapping("/index")
    public void index() throws IOException {
        Map<String,Object> map = new HashMap<>(3);
        map.put("id",1);
        map.put("name","user1");
        map.put("age",3);
        RestHighLevelClient client = ElasticClientUtil.getElasticClient();
        IndexRequest request = new IndexRequest("user_index","man","1").source(map);
        IndexResponse indexResponse = client.index(request);
        System.out.println(indexResponse);
    }
    @GetMapping("syn")
    @ResponseBody
    public RestResponse synData(String start, String end) throws IOException {
        boolean flag = goodDataSynService.synGoodData(start, end);
        return new RestResponse().success("同步成功");
    }
}
