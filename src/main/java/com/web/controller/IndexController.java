package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String toIndex(){
        return "index";
    }

    @GetMapping("/hello")
    public void sayIndex(){
        System.out.println("hello");
    }
}
