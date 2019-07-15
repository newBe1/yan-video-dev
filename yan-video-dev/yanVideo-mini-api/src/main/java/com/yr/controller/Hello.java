package com.yr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Hello {
    @GetMapping(value = "/hello")
    public String hello(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "";
        String data = restTemplate.getForObject(url,String.class);
        return "hello world";

    }
}
