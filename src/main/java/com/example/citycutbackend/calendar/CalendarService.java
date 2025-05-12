package com.example.citycutbackend.calendar;

import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import com.example.citycutbackend.treatments.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    private final TreatmentRepository treatmentRepository;

    public CalendarService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public List<CheckAvailabilityDTO> getAvailabilityForMonth(int year, int month, int stylistId, List<Integer> selectedTreatmentIds) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<CheckAvailabilityDTO> result = new ArrayList<>();

        CheckAvailabilityDTO request = new CheckAvailabilityDTO(stylistId, selectedTreatmentIds, null, false);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isAvailable = checkAvailabilityForDay(stylistId, date, request);
            result.add(new CheckAvailabilityDTO(stylistId, selectedTreatmentIds, date.toString(), isAvailable));
        }

        return result;
    }

    public int calculateTotalTime(List<Integer> selectedTreatmentIds) {
        List<Treatment> treatments = treatmentRepository.findAllById(selectedTreatmentIds);
        int total = 0;
        for (Treatment treatment : treatments) {
            total += treatment.getTimeslotAmount();
        }
        return total * 30;
    }

    public boolean checkAvailabilityForDay(int stylistId, LocalDate date, CheckAvailabilityDTO request) {
        int totalTime = calculateTotalTime(request.getSelectedTreatmentIds());

        // closed monday and sunday
        int day = date.getDayOfWeek().getValue();
        if (day == 1 || day == 7) {
            return false;
        }

        int availableTimeInMinutes = 8 * 60;
        int bookedTime = 0;

        if (availableTimeInMinutes - bookedTime >= totalTime) {
            return true;
        }

        // test data
        int hash = (date.toString() + stylistId).hashCode();
        boolean isBooked = Math.abs(hash) % 4 == 0;
        return !isBooked;
    }
}