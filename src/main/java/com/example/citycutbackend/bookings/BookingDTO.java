package com.example.citycutbackend.bookings;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookingDTO {
    private int stylistID;
    private int userID;
    private String comment;
    private List<Integer> treatmentIds;
    private List<Integer> timeslotIds;
}
