package com.example.citycutbackend.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private int userID;


    public LoginResponse(String token, int userID) {
        this.token = token;
        this.userID = userID;

    }
}
