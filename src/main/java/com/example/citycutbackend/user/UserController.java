package com.example.citycutbackend.user;

import com.example.citycutbackend.config.JwtService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Getter
@Setter
@RestController
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;

    public UserController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
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
        UserModel savedUser = null; //variabel som bliver brugt til at gemme den nye user efter database operationen
        ResponseEntity response = null; //variabel som bliver brugt til at returnere et passende HTTP response

        try {
            String hashPwd = passwordEncoder.encode(user.getPassword());//password bliver hashet (omdannet) så den ikke gemmes i klartekst
            user.setPassword(hashPwd);//den hashede password bliver sat på user
            savedUser = userRepository.save(user);//data gemmes i db
            if(savedUser.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body("User details are successfully registered");
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to: "+e.getMessage());
        }
        return response;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<UserModel> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user.getEmail());
                return ResponseEntity.ok(new LoginResponse(token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }



}
