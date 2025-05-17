package com.example.citycutbackend.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/calendar")
@CrossOrigin("*")
public class CalendarController {

    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @PostMapping("/availability")
    public List<CalendarResponseDTO> checkAvailability(
            @RequestParam("stylist") int stylistId,
            @RequestBody CalendarRequestDTO request) {
        return availabilityService.checkAvailabilityForDates(stylistId, request.getDates(),
                request.getTreatmentIds());
    }
    }
