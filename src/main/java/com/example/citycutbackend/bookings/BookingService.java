package com.example.citycutbackend.bookings;

import com.example.citycutbackend.calendar.Timeslot;
import com.example.citycutbackend.treatments.Treatment;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingDTO dto);
    /*
    List<Timeslot> selectedTimeslots, //should eventually be a dto with all these IDs
    List<Treatment> selectedTreatments, Customer customer

     */
}
