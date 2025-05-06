package com.example.citycutbackend.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {




    @GetMapping("/test")
    public String getTest() {
        return "Hello";
    }
}
