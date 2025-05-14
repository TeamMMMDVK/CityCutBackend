package com.example.citycutbackend.calendar;

import com.example.citycutbackend.treatments.Treatment;

import java.time.LocalDateTime;
import java.util.List;

public class TimeslotServiceImpl implements TimeslotService{
    @Override
    public List<Timeslot> getAvailableTimeslotsForBooking(CheckAvailabilityDTO checkAvailabilityDTO) {
        return null;
    }

    @Override
    public List<Timeslot> getAvailableTimeslotsOfStylist(Stylist stylist) {
        return null;
    }
}
