package com.example.citycutbackend.calendar;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Data
@NoArgsConstructor
public class CheckAvailabilityDTO {
    private int stylistId;
    private List<Integer> selectedTreatmentIds;
    private String date;
    private boolean available;

    public CheckAvailabilityDTO(int stylistId, List<Integer> selectedTreatmentIds, String date, boolean available) {
        this.stylistId = stylistId;
        this.selectedTreatmentIds = selectedTreatmentIds;
        this.date = date;
        this.available = available;
    }
}
