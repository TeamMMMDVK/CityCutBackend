package com.example.citycutbackend.calendar;

import com.example.citycutbackend.config.JwtAuthFilter;
import com.example.citycutbackend.config.JwtService;
import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AvailabilityServiceImplTest {

    //For at test kan køre korrekt, må jeg lave Mock af disse bean ifm jwt ellers
    //vil det ikke compile korrekt. Når test kører så loades hele applikationen nemlig,
    //også sikkerhed, og disse sikkerheds-beans mangler ....derfor mockes de
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    private Treatment treatment1;
    private Treatment treatment2;

    private Timeslot ts1, ts2, ts3, ts4;
    LocalDate date = LocalDate.now();

    @BeforeEach
    void setup() {
        // Create treatments
        treatment1 = new Treatment();
        treatment1.setTimeslotAmount(1);
        treatment1 = treatmentRepository.save(treatment1);

        treatment2 = new Treatment();
        treatment2.setTimeslotAmount(2);
        treatment2 = treatmentRepository.save(treatment2);



        // Create available timeslots, duration 30, no booking (means free)
        ts1 = new Timeslot(0, date, LocalTime.of(10, 0), null, true, 30);
        ts2 = new Timeslot(0, date, LocalTime.of(10, 30), null, true, 30);
        ts3 = new Timeslot(0, date, LocalTime.of(11, 0), null, true, 30);
        ts4 = new Timeslot(0, date, LocalTime.of(11, 30), null, true, 30);

        ts1 = timeslotRepository.save(ts1);
        ts2 = timeslotRepository.save(ts2);
        ts3 = timeslotRepository.save(ts3);
        ts4 = timeslotRepository.save(ts4);
    }

    @Test
    void whenSingleTimeslotNeeded_returnAllAvailableTimeslots() {
        List<Integer> treatmentIds = List.of(treatment1.getId()); // needs 1 timeslot
        List<AvailableTimeslotDTO> results = availabilityService.getAvailableTimeslotsForDay(1, treatmentIds, LocalDate.now().toString());

        assertThat(results).hasSize(4);
        assertThat(results).extracting("id")
                .containsExactlyInAnyOrder(ts1.getId(), ts2.getId(), ts3.getId(), ts4.getId());
    }

    @Test
    void whenMultipleTimeslotsNeeded_returnOnlyStartsOfTimeslotsInARow() {
        List<Integer> treatmentIds = List.of(treatment2.getId()); // needs 2 timeslots

        List<AvailableTimeslotDTO> results = availabilityService.getAvailableTimeslotsForDay(1, treatmentIds, LocalDate.now().toString());

        // Expect starting timeslots where 2 slots are available in a row: ts1, ts2, ts3
        assertThat(results).hasSize(3);
        assertThat(results).extracting("time")
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(10, 30), LocalTime.of(11, 0));
    }

    @Test
    void whenNoTreatments_returnEmptyList() {
        List<AvailableTimeslotDTO> results = availabilityService.getAvailableTimeslotsForDay(1, Collections.emptyList(), LocalDate.now().toString());
        assertThat(results).isEmpty();
    }

    @Test
    void whenTreatmentNotFound_ignoreAndProcessOthers() {
        List<Integer> treatmentIds = List.of(9999, treatment1.getId()); // 9999 does not exist

        List<AvailableTimeslotDTO> results = availabilityService.getAvailableTimeslotsForDay(1, treatmentIds, LocalDate.now().toString());

        // Only treatment1's slots count: 1 slot needed, so all available returned
        assertThat(results).hasSize(4);
    }

    @Test
    void whenMultipleSlotsNeeded_shouldCallFindTimeslotsInARow() {
        // Arrange
        Treatment treatment = new Treatment();
        treatment.setTitle("Cut & Color");
        treatment.setDescription("Combo");
        treatment.setTimeslotAmount(2); // Require 2 timeslots in a row
        treatment.setPrice(499.0);
        treatment = treatmentRepository.save(treatment);

        List<Integer> selectedTreatmentIds = List.of(treatment.getId());

        // Act
        List<AvailableTimeslotDTO> result = availabilityService.getAvailableTimeslotsForDay(1, selectedTreatmentIds, date.toString());
        for(AvailableTimeslotDTO dto : result){
            System.out.println(dto);
        }
        // Assert
        assertEquals(3, result.size());
        AvailableTimeslotDTO dto = result.get(0);
        assertEquals(LocalTime.of(10, 0), dto.getTime());
    }
}