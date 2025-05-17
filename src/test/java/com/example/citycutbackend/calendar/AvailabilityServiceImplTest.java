package com.example.citycutbackend.calendar;

import com.example.citycutbackend.config.JwtAuthFilter;
import com.example.citycutbackend.config.JwtService;
import com.example.citycutbackend.user.UserRepository;
import com.example.citycutbackend.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AvailabilityServiceImplTest {

    //For at test kan køre korrekt, må jeg lave Mock af disse bean ifm jwt ellers
    //vil det ikke compile korrekt. Når test kører så loades hele applikationen nemlig,
    //også sikkerhed, og disse sikkerheds-beans mangler ....derfor mockes de
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @Test
    void getAvailableTimeslotsForDay() {
    }

    @Test
    void checkAvailabilityForDates() {
    }
}