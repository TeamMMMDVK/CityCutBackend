package com.example.citycutbackend.calendar;

import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final TreatmentRepository treatmentRepository;
    private final TimeslotRepository timeslotRepository;
    private static final Logger logger = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    public AvailabilityServiceImpl(TreatmentRepository treatmentRepository, TimeslotRepository timeslotRepository) {
        this.treatmentRepository = treatmentRepository;
        this.timeslotRepository = timeslotRepository;
    }


    @Override
    public List<AvailableTimeslotDTO> getAvailableTimeslotsForDay(int stylistId,
                                                                  List<Integer> selectedTreatmentIds, String date) {
        int totalAmountOfTimeslotsNeeded = 0;
        logger.info("stylist id: " + stylistId);
        logger.info("date: " + date);
        for (int id : selectedTreatmentIds) {
            Optional<Treatment> optionalTreatment = treatmentRepository.findById(id);
            if (optionalTreatment.isPresent()) {
                Treatment treatment = optionalTreatment.get();
                logger.info("treatment: " + treatment);
                logger.info("amount of treatment timeslots: " + treatment.getTimeslotAmount());
                totalAmountOfTimeslotsNeeded += treatment.getTimeslotAmount();
                logger.info("totalAmountOfTimeslotsNeeded: " + totalAmountOfTimeslotsNeeded);
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
                if (!isNextTimeslotRightAfterCurrent) break;
            }
            if (isNextTimeslotRightAfterCurrent)
                availableTimeslots.add(new AvailableTimeslotDTO(currentTimeslot.getDate(),
                        currentTimeslot.getTime()));

        }
        for (AvailableTimeslotDTO dto : availableTimeslots) {
            logger.info("timeslot available: " + dto);
        }
        return availableTimeslots;
    }
    public List<CalendarResponseDTO> checkAvailabilityForDates(int stylistId, List<String> dates, List<Integer> treatmentIds) {
        List<CalendarResponseDTO> results = new ArrayList<>();
        if (dates == null || dates.isEmpty() || treatmentIds == null || treatmentIds.isEmpty())
            return results;

        int totalNeeded = 0;
        for (Integer id : treatmentIds) {
            Optional<Treatment> treatment = treatmentRepository.findById(id);
            if (treatment.isPresent())
                totalNeeded += treatment.get().getTimeslotAmount();
        }

        for (String date : dates) {

            List<AvailableTimeslotDTO> availableTimeslotsForDay = getAvailableTimeslotsForDay(stylistId, treatmentIds, date);
            if (availableTimeslotsForDay == null || availableTimeslotsForDay.isEmpty()) {
                results.add(new CalendarResponseDTO(date, CalendarStatus.FULL, false));
                continue;
            }

            boolean isAvailable = availableTimeslotsForDay.size() >= totalNeeded;
            CalendarStatus status = isAvailable ? CalendarStatus.AVAILABLE : CalendarStatus.PARTIAL;
            results.add(new CalendarResponseDTO(date, status, isAvailable));
        }

        return results;
    }}
