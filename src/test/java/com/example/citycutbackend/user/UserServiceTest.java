package com.example.citycutbackend.user;

import com.example.citycutbackend.config.JwtAuthFilter;
import com.example.citycutbackend.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    //For at test kan køre korrekt, må jeg lave Mock af disse bean ifm jwt ellers
    //vil det ikke compile korrekt. Når test kører så loades hele applikationen nemlig,
    //også sikkerhed, og disse sikkerheds-beans mangler ....derfor mockes de
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setup() {
        userRepository.deleteAll(); // Ryd databasen før hver test
    }

    @Test
    void registerUserSuccesfully() {

        //Arrange
        UserModel u1 = new UserModel("user1","user1@mail.dk","password123","ROLE_CUSTOMER");

        //Act
        ResponseEntity<String> response = userService.registerUser(u1);
        var actualResponseStatus = response.getStatusCode();
        var expectedResponseStatus = HttpStatus.CREATED;

        var actualResponseBody = response.getBody();
        var expectedResponseBody = "Bruger blev succesfuldt oprettet";

        //Assert
        assertEquals(expectedResponseStatus,actualResponseStatus);
        assertEquals(expectedResponseBody,actualResponseBody);
    }

    @Test
    void registerUserAlreadyUser() {

        //Arrange
        UserModel u1 = new UserModel("user1","user1@mail.dk","password123","ROLE_CUSTOMER");
        UserModel u2 = new UserModel("user2","user1@mail.dk","password123","ROLE_CUSTOMER");

        //Act
        userService.registerUser(u1);
        ResponseEntity<String> responseU2 = userService.registerUser(u2);
        var actualResponseStatus = responseU2.getStatusCode();
        var expectedResponseStatus = HttpStatus.CONFLICT;

        var actualResponseBody = responseU2.getBody();
        var expectedResponseBody = "En bruger med denne email findes allerede";

        //Assert
        assertEquals(expectedResponseStatus,actualResponseStatus);
        assertEquals(expectedResponseBody,actualResponseBody);
    }
}