package com.example.citycutbackend.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AvailableTimeslotDTO {
    private int id;
    private LocalDate date;
    private LocalTime time;
}
