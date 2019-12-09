package com.web.controller;

import com.web.service.GoodDataSynService;
import com.web.util.ElasticClientUtil;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public void synData() throws IOException {
        String start = "2019-01-01 00:00:00";
        String end = "2019-12-12 00:00:00";
        boolean flag = goodDataSynService.synGoodData(start, end);
    }
}
