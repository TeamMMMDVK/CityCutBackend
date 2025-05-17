package com.example.citycutbackend.calendar;
import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    List<AvailableTimeslotDTO> getAvailableTimeslotsForDay(int stylistId, List<Integer> selectedTreatmentIds, String date);

    List<CalendarResponseDTO> checkAvailabilityForDates(int stylistId, List<String> dates, List<Integer> treatmentIds);
}
