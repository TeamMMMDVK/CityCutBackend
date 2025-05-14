package com.example.citycutbackend.calendar;
import java.util.List;

public interface AvailabilityService {
    List<AvailableTimeslotDTO> getAvailableTimeslotsForDay(int stylistId, List<Integer> selectedTreatmentIds, String date);

    List<CheckAvailabilityDTO> getAvailableTimeslotsForMonth(int year, int month, int stylistId,
                                                             List<Integer> selectedTreatmentIds); //by default for a month
}
