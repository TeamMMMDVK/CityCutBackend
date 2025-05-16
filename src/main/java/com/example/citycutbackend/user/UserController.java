package com.example.citycutbackend.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Getter
@Setter
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private UserServiceImpl userService;


    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

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
    public ResponseEntity<String> registerUser(@RequestBody UserModel user){
        return userService.registerUser(user);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }



}
