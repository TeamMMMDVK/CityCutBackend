package com.example.citycutbackend.calendar;

import com.example.citycutbackend.config.JwtAuthFilter;
import com.example.citycutbackend.config.JwtService;
import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CalendarServiceTest {

    //For at test kan køre korrekt, må jeg lave Mock af disse bean ifm jwt ellers
    //vil det ikke compile korrekt. Når test kører så loades hele applikationen nemlig,
    //også sikkerhed, og disse sikkerheds-beans mangler ....derfor mockes de
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private CalendarService calendarService;

    @MockBean
    private TreatmentRepository treatmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAvailabilityForMonth() throws Exception {
        int stylistId = 1;
        List<Integer> selectedTreatmentIds = Arrays.asList(1, 2);

        Treatment treatment1 = new Treatment();
        treatment1.setId(1);
        treatment1.setTimeslotAmount(1);
        treatment1.setPrice(50.0);
        treatment1.setTitle("Haircut");

        Treatment treatment2 = new Treatment();
        treatment2.setId(2);
        treatment2.setTimeslotAmount(2);
        treatment2.setPrice(100.0);
        treatment2.setTitle("Shave");

        when(treatmentRepository.findAllById(selectedTreatmentIds)).thenReturn(Arrays.asList(treatment1, treatment2));

        List<CheckAvailabilityDTO> availability = calendarService.getAvailabilityForMonth(2025, 5, stylistId, selectedTreatmentIds);

        int totalTimeForTreatments = calendarService.calculateTotalTime(selectedTreatmentIds);
        System.out.println("Total Time for Selected Treatments: " + totalTimeForTreatments + " minutes");

        assertNotNull(availability);
        System.out.println(availability.size());
        assertEquals(31, availability.size());
        assertTrue(availability.stream().anyMatch(a -> a.getDate().equals("2025-05-01") && a.isAvailable()));
        assertFalse(availability.stream().anyMatch(a -> a.getDate().equals("2025-05-31") && !a.isAvailable()));
    }

    @Test
    public void testCalculateTotalTime() {
        List<Integer> selectedTreatmentIds = Arrays.asList(1, 2);

        Treatment treatment1 = new Treatment();
        treatment1.setId(1);
        treatment1.setTimeslotAmount(1);

        Treatment treatment2 = new Treatment();
        treatment2.setId(2);
        treatment2.setTimeslotAmount(2);

        when(treatmentRepository.findAllById(selectedTreatmentIds)).thenReturn(Arrays.asList(treatment1, treatment2));

        int totalTime = calendarService.calculateTotalTime(selectedTreatmentIds);
        System.out.println("Total Time: " + totalTime + " minutes");

        assertEquals(90, totalTime);
    }

    @Test
    public void testCheckAvailabilityForDay() {
        int stylistId = 1;
        LocalDate date = LocalDate.of(2025, 5, 1);
        List<Integer> selectedTreatmentIds = Arrays.asList(1, 2);

        Treatment treatment1 = new Treatment();
        treatment1.setId(1);
        treatment1.setTimeslotAmount(1);

        Treatment treatment2 = new Treatment();
        treatment2.setId(2);
        treatment2.setTimeslotAmount(2);

        when(treatmentRepository.findAllById(selectedTreatmentIds)).thenReturn(Arrays.asList(treatment1, treatment2));

        boolean isAvailable = calendarService.checkAvailabilityForDay(stylistId, date, new CheckAvailabilityDTO(stylistId, selectedTreatmentIds, date.toString(), false));
        System.out.println("Availability for " + date + ": " + (isAvailable ? "Available" : "Not Available"));

        assertTrue(isAvailable);
    }
}