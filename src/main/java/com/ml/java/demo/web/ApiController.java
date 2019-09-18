package com.ml.java.demo.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Service;

@Service
@RestController
public class ApiController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}