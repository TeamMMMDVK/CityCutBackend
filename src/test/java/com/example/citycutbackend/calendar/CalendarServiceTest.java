package com.example.citycutbackend.calendar;

import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
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

@SpringBootTest()
@ActiveProfiles("test")
public class CalendarServiceTest {

    @Autowired
    private CalendarService calendarService;

    @MockBean
    private TreatmentRepository treatmentRepository;

    @Test
    public void testGetAvailabilityForMonth() {
        // Arrange
        int stylistId = 1;
        List<Integer> selectedTreatmentIds = Arrays.asList(1, 2);

        // Mock the behavior of TreatmentRepository
        Treatment treatment1 = new Treatment();
        treatment1.setId(1);
        treatment1.setTimeslotAmount(1); // 1 slot
        treatment1.setPrice(50.0);
        treatment1.setTitle("Haircut");
        treatment1.setDescription("Short haircut");

        Treatment treatment2 = new Treatment();
        treatment2.setId(2);
        treatment2.setTimeslotAmount(2); // 2 slots
        treatment2.setPrice(100.0);
        treatment2.setTitle("Shave");
        treatment2.setDescription("Beard shave");

        when(treatmentRepository.findAllById(selectedTreatmentIds)).thenReturn(Arrays.asList(treatment1, treatment2));

        // Act
        List<CheckAvailabilityDTO> availability = calendarService.getAvailabilityForMonth(2025, 5, stylistId, selectedTreatmentIds);

        // Calculate the total time for selected treatments
        int totalTimeForTreatments = calendarService.calculateTotalTime(selectedTreatmentIds);
        System.out.println("Total Time for Selected Treatments: " + totalTimeForTreatments + " minutes");

        // Output availability for each day and the available time per day
        availability.forEach(a -> {
            System.out.println("Date: " + a.getDate() + " | Available: " + a.isAvailable());
            System.out.println("Current Time: " + LocalDateTime.now());
            System.out.println("Total Available Time for " + a.getDate() + ": 480 minutes (8 hours)");
            System.out.println("Required Time for Treatments on " + a.getDate() + ": " + totalTimeForTreatments + " minutes");
            if (a.getDate().equals("2025-05-31")) {
                System.out.println("Explicitly marking May 31st as unavailable with 0 minutes available.");
            }
            System.out.println("-----------------------------------------------------");
        });

        // Assert
        assertNotNull(availability);
        assertEquals(31, availability.size()); // May has 31 days
        assertTrue(availability.stream().anyMatch(a -> a.getDate().equals("2025-05-01") && a.isAvailable())); // Example check
        assertFalse(availability.stream().anyMatch(a -> a.getDate().equals("2025-05-31") && a.isAvailable())); // May 31st should be unavailable and have 0 minutes available
    }

    @Test
    public void testCalculateTotalTime() {
        // Arrange
        List<Integer> selectedTreatmentIds = Arrays.asList(1, 2);

        // Mock the behavior of TreatmentRepository
        Treatment treatment1 = new Treatment();
        treatment1.setId(1);
        treatment1.setTimeslotAmount(1); // 1 slot

        Treatment treatment2 = new Treatment();
        treatment2.setId(2);
        treatment2.setTimeslotAmount(2); // 2 slots

        when(treatmentRepository.findAllById(selectedTreatmentIds)).thenReturn(Arrays.asList(treatment1, treatment2));

        // Act
        int totalTime = calendarService.calculateTotalTime(selectedTreatmentIds);

        // Output the calculated total time
        System.out.println("Total Time: " + totalTime + " minutes");

        // Assert
        assertEquals(90, totalTime); // 1 slot * 30 + 2 slots * 30 = 90 minutes
    }

    @Test
    public void testCheckAvailabilityForDay() {
        // Arrange
        int stylistId = 1;
        LocalDate date = LocalDate.of(2025, 5, 1);
        List<Integer> selectedTreatmentIds = Arrays.asList(1, 2);

        // Mock the behavior of TreatmentRepository
        Treatment treatment1 = new Treatment();
        treatment1.setId(1);
        treatment1.setTimeslotAmount(1);

        Treatment treatment2 = new Treatment();
        treatment2.setId(2);
        treatment2.setTimeslotAmount(2);

        when(treatmentRepository.findAllById(selectedTreatmentIds)).thenReturn(Arrays.asList(treatment1, treatment2));

        // Act
        boolean isAvailable = calendarService.checkAvailabilityForDay(stylistId, date, new CheckAvailabilityDTO(stylistId, selectedTreatmentIds, date.toString(), false));

        // Output availability check result with current time
        System.out.println("Availability for " + date + ": " + (isAvailable ? "Available" : "Not Available"));
        System.out.println("Current Time: " + LocalDateTime.now());

        // Assert
        assertTrue(isAvailable); // You can change this based on your mock data and logic
    }
}