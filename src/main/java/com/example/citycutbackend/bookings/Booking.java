package com.example.citycutbackend.bookings;

import com.example.citycutbackend.calendar.Stylist;
import com.example.citycutbackend.calendar.Timeslot;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference("customer-bookings")
    private Customer customer;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "stylist_id")
    @JsonBackReference("stylist-bookings")
    private Stylist stylist;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("booking-timeslots")
    private List<Timeslot> timeslots;
}
