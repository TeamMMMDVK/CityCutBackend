package com.example.citycutbackend.calendar;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeslotDTO {
    private Long stylistId;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
