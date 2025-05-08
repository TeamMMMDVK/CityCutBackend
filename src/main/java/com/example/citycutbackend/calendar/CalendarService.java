package com.example.citycutbackend.calendar;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalendarService {

    public List<Calendar> getAvailabilityForMonth(int year, int month, int stylistId) {
        // TODO: Fetch availability from DB based on stylist and month
        return List.of();
    }

    public List<Calendar> getTimeslotsForDay(LocalDate date, int stylistId) {
        // TODO: Fetch timeslots from DB for a specific date and stylist
        return List.of();
    }
}