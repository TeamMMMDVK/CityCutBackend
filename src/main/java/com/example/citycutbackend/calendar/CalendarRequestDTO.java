package com.example.citycutbackend.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarRequestDTO {
    private List<Integer> treatmentIds;
    private List<String> dates;
}