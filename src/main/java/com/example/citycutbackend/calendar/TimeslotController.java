package com.example.citycutbackend.calendar;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/timeslots")
@CrossOrigin("*")
public class TimeslotController {

    private final AvailabilityService availabilityService;

    public TimeslotController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping()
    public List<AvailableTimeslotDTO> getAvailableTimeslotsForDay(@RequestParam("stylist") int stylistId,
                                                                   @RequestParam("treatments") List<Integer> selectedTreatmentIds,
                                                                  @RequestParam("date") String date){
        return availabilityService.getAvailableTimeslotsForDay(stylistId, selectedTreatmentIds, date);
    }
}
