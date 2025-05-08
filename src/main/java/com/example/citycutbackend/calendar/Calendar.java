package com.example.citycutbackend.calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate date;
    private LocalTime time;
    private boolean hasAvailability;

    // You can also link this to the Stylist if necessary
    // @ManyToOne
    // @JoinColumn(name = "stylist_id", referencedColumnName = "id")
    // private Stylist stylist;
}