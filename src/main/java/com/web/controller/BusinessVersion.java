package com.web.controller;

import com.web.service.ActiveMQService;
import com.web.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("business")
public class BusinessVersion {
    @Autowired
    public BusinessService businessService;
    @Autowired
    private ActiveMQService activeMQService;
    @GetMapping("version/{id}")
    public Object getVersion(@PathVariable int id){
        Integer version = businessService.getVersionById(id);
        return version;
    }
    @GetMapping("send")
    public void sendMessage(){
        activeMQService.sendTopicMessage();
        activeMQService.sendQueMessage();
    }
}
