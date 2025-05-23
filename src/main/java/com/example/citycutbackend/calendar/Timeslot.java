package com.example.citycutbackend.calendar;

import com.example.citycutbackend.bookings.Booking;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "timeslots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private LocalTime time;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference("booking-timeslots")
    private Booking booking;
    boolean isAvailable;
    int duration;
}
