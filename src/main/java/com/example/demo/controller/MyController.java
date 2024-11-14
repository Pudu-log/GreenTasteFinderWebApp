package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
    @GetMapping("/")
    public String index() {
        return "index";
        
    }
    
    @GetMapping("/1")
    public String index1() {
        return "index1";
    }
    @GetMapping("/2")
    public String index2() {
        return "index2";
    }
}
