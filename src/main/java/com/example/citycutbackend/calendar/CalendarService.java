package com.example.citycutbackend.calendar;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    public List<Calendar> getAvailabilityForMonth(int year, int month, int stylistId, List<Integer> selectedTreatmentIds) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        //return generateAvailabilityForMonth(startDate, endDate, stylistId, selectedTreatmentIds);
    }

    private int calculateTotalTime(List<Integer> selectedTreatmentIds) {
        return selectedTreatmentIds.size() * 30; // Assume 30 minutes per treatment
    }
}