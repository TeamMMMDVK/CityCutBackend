package com.example.citycutbackend.bookings;

import com.example.citycutbackend.calendar.Stylist;
import com.example.citycutbackend.calendar.Timeslot;
import com.example.citycutbackend.treatments.Treatment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="booking_treatments",
            joinColumns = @JoinColumn(name="booking_id"),
            inverseJoinColumns = @JoinColumn(name = "treatment_id")
    )
    @JsonManagedReference("booking-treatments")
    private List<Treatment> treatments;
}
