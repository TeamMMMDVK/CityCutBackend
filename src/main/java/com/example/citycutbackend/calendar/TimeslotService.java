package com.example.citycutbackend.calendar;

import com.example.citycutbackend.treatments.Treatment;

import java.time.LocalDateTime;
import java.util.List;

public interface TimeslotService {
    List<Timeslot> getAvailableTimeslotsForBooking(CheckAvailabilityDTO checkAvailabilityDTO);

    List<Timeslot> getAvailableTimeslotsOfStylist(Stylist stylist); //by default for a month
}
