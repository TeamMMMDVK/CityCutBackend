package com.example.citycutbackend.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResponseDTO {
    private String date;
    private CalendarStatus status;
    private boolean available;
}
