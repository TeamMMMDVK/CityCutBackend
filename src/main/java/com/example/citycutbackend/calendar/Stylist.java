package com.example.citycutbackend.calendar;

import com.example.citycutbackend.bookings.Booking;
import com.example.citycutbackend.calendar.Timeslot;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "stylists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stylist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "stylist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("stylist-bookings")
    private List<Booking> bookings;
}
