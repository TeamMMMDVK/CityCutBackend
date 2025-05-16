package com.example.citycutbackend.user;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<String> registerUser(UserModel user);

    ResponseEntity<?> login(LoginRequest request);
}
