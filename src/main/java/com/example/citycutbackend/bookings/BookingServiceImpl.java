package com.example.citycutbackend.bookings;

import com.example.citycutbackend.calendar.Timeslot;
import com.example.citycutbackend.treatments.Treatment;

import java.util.List;

public class BookingServiceImpl implements BookingService{
    @Override
    public Booking createBooking(List<Timeslot> selectedTimeslots,
                                 List<Treatment> selectedTreatments, Customer customer) {
        return null;
    }
}
