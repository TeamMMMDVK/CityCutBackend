package com.example.citycutbackend.calendar;

import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final TreatmentRepository treatmentRepository;
    private final TimeslotRepository timeslotRepository;

    public AvailabilityServiceImpl(TreatmentRepository treatmentRepository, TimeslotRepository timeslotRepository) {
        this.treatmentRepository = treatmentRepository;
        this.timeslotRepository = timeslotRepository;
    }


    @Override
    public List<AvailableTimeslotDTO> getAvailableTimeslotsForDay(int stylistId, List<Integer> selectedTreatmentIds, String date) {
        int totalAmountOfTimeslotsNeeded = 0;
        for (int id : selectedTreatmentIds) {
            Optional<Treatment> optionalTreatment = treatmentRepository.findById(id);
            if (optionalTreatment.isPresent()) {
                Treatment treatment = optionalTreatment.get();
                totalAmountOfTimeslotsNeeded += treatment.getTimeslotAmount();
            }
        }
        List<Timeslot> allAvailableTimeslotsForDay = timeslotRepository.fetchAllAvailableTimeslotsForDay(LocalDate.parse(date));
        List<AvailableTimeslotDTO> availableTimeslots = new ArrayList<>();

        if (totalAmountOfTimeslotsNeeded == 1) return allAvailableTimeslotsForDay.stream().map(timeslot ->
                new AvailableTimeslotDTO(timeslot.getDate(), timeslot.getTime())).collect(Collectors.toList());


        for (int i = 0; i <= allAvailableTimeslotsForDay.size() - totalAmountOfTimeslotsNeeded; i++) {
            Timeslot currentTimeslot = allAvailableTimeslotsForDay.get(i);
            boolean isNextTimeslotRightAfterCurrent = false;
            for (int j = 1; j < totalAmountOfTimeslotsNeeded; j++) {
               Timeslot toCheck = allAvailableTimeslotsForDay.get(i + j);
                isNextTimeslotRightAfterCurrent = toCheck.getTime().equals(currentTimeslot.getTime().plusMinutes(30 * j));
                if(!isNextTimeslotRightAfterCurrent) break;
            }
            if(isNextTimeslotRightAfterCurrent)availableTimeslots.add(new AvailableTimeslotDTO(currentTimeslot.getDate(),
                    currentTimeslot.getTime()));

        }

        return availableTimeslots;
    }


    @Override
    public List<CheckAvailabilityDTO> getAvailableTimeslotsForMonth(int year, int month, int stylistId, List<Integer> selectedTreatmentIds) {
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
