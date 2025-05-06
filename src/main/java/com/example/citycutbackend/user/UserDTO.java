package com.example.citycutbackend.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String name;
    private String email;
    private String password;
    private int roleId; //det skal kun være muligt for admin at tildele rolle

}
