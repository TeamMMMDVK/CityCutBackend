package com.example.citycutbackend.calendar;

import jakarta.persistence.Entity;
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
    private int id;
    private LocalDate date;
    private LocalTime time;
    private boolean hasAvailability;

    //@ManyToOne
    //@JoinColumn(name = "stylist_id", referencedColumnName = "id")
    //private Stylist stylist; // Assuming you're linking it to a stylist
}