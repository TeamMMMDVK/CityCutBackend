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

    @GetMapping("/availability/{year}/{month}")
    public List<Calendar> getAvailability(@PathVariable int year, @PathVariable int month,
                                          @RequestParam("stylistId") int stylistId) {
        return calendarService.getAvailabilityForMonth(year, month, stylistId);
    }

    @GetMapping("/day/{date}")
    public List<Calendar> getTimeslots(@PathVariable String date,
                                       @RequestParam("stylistId") int stylistId) {
        LocalDate localDate = LocalDate.parse(date);
        return calendarService.getTimeslotsForDay(localDate, stylistId);
    }
}