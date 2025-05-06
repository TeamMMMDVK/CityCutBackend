package com.example.citycutbackend.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {



    //-----------TEST ENDPOINTS--------------
    @GetMapping("/test1")
    public String getTest1() {
        return "Her har alle adgang uden login";
    }

    @GetMapping("/test2")
    public String getTest2() {
        return "Her skal logges ind og både kunde plus admin har adgang";
    }

    @GetMapping("/test3")
    public String getTest3() {
        return "Her skal logges ind og kun admin rolle har adgang";
    }

    //----------------------------------------

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO){

    }
}
