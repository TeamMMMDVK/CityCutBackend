package com.example.citycutbackend.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/calendar")
@CrossOrigin("*")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;


    @PostMapping("/availability/{year}/{month}")
    public List<CheckAvailabilityDTO> getAvailability(@PathVariable int year, @PathVariable int month,
                                                      @RequestBody CheckAvailabilityDTO request) {
        return calendarService.getAvailabilityForMonth(
                year,
                month,
                request.getStylistId(),
                request.getSelectedTreatmentIds()
        );
    }
        @PostMapping("/check-availability")
        public boolean checkAvailability(@RequestParam int stylistId,
                                         @RequestParam LocalDate date,
                                         @RequestBody CheckAvailabilityDTO request) {
            return calendarService.checkAvailabilityForDay(stylistId, date, request);
        }
    }
