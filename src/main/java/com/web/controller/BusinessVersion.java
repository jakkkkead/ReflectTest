package com.web.controller;

import com.web.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("business")
public class BusinessVersion {
    @Autowired
    public BusinessService businessService;
    @GetMapping("version/{id}")
    public Object getVersion(@PathVariable int id){
        Integer version = businessService.getVersionById(id);
        return version;
    }
}
