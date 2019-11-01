package com.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.web.bean.User;
import com.web.util.ElasticClientUtil;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/es")
public class ElasticController {
    @GetMapping("/index")
    public void index(){
        Map<String,Object> map = new HashMap<>(3);
        map.put("id",1);
        map.put("name","user1");
        map.put("age",3);
        TransportClient client = ElasticClientUtil.getElasticClient();
        IndexResponse indexResponse = client.prepareIndex("user_index","user","1").setSource(map).get();
        System.out.println(indexResponse);
    }
}
