package com.example.citycutbackend.calendar;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
@Getter
@Service
public class CheckAvailabilityDTO {
    private int stylistId;
    private List<Integer> selectedTreatmentIds;

}
