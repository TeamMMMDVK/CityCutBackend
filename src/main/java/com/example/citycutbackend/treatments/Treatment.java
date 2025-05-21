package com.example.citycutbackend.treatments;

import com.example.citycutbackend.bookings.Booking;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "treatments")
@Data
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany(mappedBy = "treatments")
    private List<Booking> bookings;
    private String title;
    private String description;
    private int timeslotAmount;
    private double price;
}
