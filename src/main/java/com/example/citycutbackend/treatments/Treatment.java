package com.example.citycutbackend.treatments;

import com.example.citycutbackend.bookings.Booking;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "treatments")
@Getter
@Setter
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany(mappedBy = "treatments")
    @JsonBackReference("booking-treatments")
    private List<Booking> bookings;
    private String title;
    private String description;
    private int timeslotAmount;
    private double price;
}
