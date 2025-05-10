package com.example.citycutbackend.treatments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;

@Entity(name = "treatments")
@Data
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String description;

    @Column(name = "timeslotAmount") // Explicitly map the Java field to the database column
    private int timeslotAmount;

    private double price;
}