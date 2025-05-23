package com.example.citycutbackend.calendar;

import com.example.citycutbackend.bookings.Booking;
import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final TreatmentRepository treatmentRepository;
    private final TimeslotRepository timeslotRepository;
    private final StylistRepository stylistRepository;
    private static final Logger logger = LoggerFactory.getLogger(AvailabilityServiceImpl.class);

    public AvailabilityServiceImpl(TreatmentRepository treatmentRepository, TimeslotRepository timeslotRepository, StylistRepository stylistRepository) {
        this.treatmentRepository = treatmentRepository;
        this.timeslotRepository = timeslotRepository;
        this.stylistRepository = stylistRepository;
    }


    @Override
    public List<AvailableTimeslotDTO> getAvailableTimeslotsForDay(int stylistId,
                                                                  List<Integer> selectedTreatmentIds,
                                                                  String date) {
        logger.info("stylist id: " + stylistId);
        logger.info("date: " + date);

        int totalSlotsNeeded = calculateTotalTimeslotsNeeded(selectedTreatmentIds);
        List<Timeslot> allAvailableTimeslots = fetchAvailableTimeslots(date);

        if (totalSlotsNeeded == 1) {
            return convertToDTOs(allAvailableTimeslots);
        }

        List<AvailableTimeslotDTO> availableTimeslots = findTimeslotsInARow(allAvailableTimeslots, totalSlotsNeeded);

        for (AvailableTimeslotDTO dto : availableTimeslots) {
            logger.info("timeslot available: " + dto);
        }

        return availableTimeslots;
    }

    private int calculateTotalTimeslotsNeeded(List<Integer> treatmentIds) {
        int total = 0;
        for (int id : treatmentIds) {
            Optional<Treatment> optionalTreatment = treatmentRepository.findById(id);
            if (optionalTreatment.isPresent()) {
                Treatment treatment = optionalTreatment.get();
                logger.info("treatment: " + treatment);
                logger.info("amount of treatment timeslots: " + treatment.getTimeslotAmount());
                total += treatment.getTimeslotAmount();
                logger.info("totalAmountOfTimeslotsNeeded: " + total);
            }
        }
        return total;
    }

    private List<Timeslot> fetchAvailableTimeslots(String date) {
        return timeslotRepository.fetchAllAvailableTimeslotsForDay(LocalDate.parse(date));
    }

    private List<AvailableTimeslotDTO> convertToDTOs(List<Timeslot> timeslots) {
        return timeslots.stream()
                .map(ts -> new AvailableTimeslotDTO(ts.getId(), ts.getDate(), ts.getTime()))
                .collect(Collectors.toList());
    }

    private List<AvailableTimeslotDTO> findTimeslotsInARow(List<Timeslot> timeslots, int neededSlots) {
        List<AvailableTimeslotDTO> available = new ArrayList<>();

        if (neededSlots <= 0 || timeslots.size() < neededSlots) {
            return available;
        }

        for (int i = 0; i <= timeslots.size() - neededSlots; i++) {
            Timeslot start = timeslots.get(i);
            boolean allInARow = true;

            for (int j = 1; j < neededSlots; j++) {
                if (i + j >= timeslots.size()) {
                    allInARow = false;
                    break;
                }

                Timeslot next = timeslots.get(i + j);
                if (!next.getTime().equals(start.getTime().plusMinutes(30L * j))) {
                    allInARow = false;
                    break;
                }
            }

            if (allInARow) {
                available.add(new AvailableTimeslotDTO(start.getId(), start.getDate(), start.getTime()));
            }
        }

        return available;
    }


    public List<CalendarResponseDTO> checkAvailabilityForDates(int stylistId, List<String> dates, List<Integer> treatmentIds) {
        List<CalendarResponseDTO> results = new ArrayList<>();
//        if (dates == null || dates.isEmpty() || treatmentIds == null || treatmentIds.isEmpty())
//            return results;
        for (String date : dates) {
            List<AvailableTimeslotDTO> availableTimeslotsForDay = getAvailableTimeslotsForDay(stylistId, treatmentIds, date);
            int available = availableTimeslotsForDay.size();
            logger.info("Date: " + date + ", available: " + available);

            if (available == 0) {
                results.add(new CalendarResponseDTO(date, CalendarStatus.FULL, false));
            } else if (available == 1) {
                results.add(new CalendarResponseDTO(date, CalendarStatus.PARTIAL, true));
            } else {
                results.add(new CalendarResponseDTO(date, CalendarStatus.AVAILABLE, true));
            }
        }
        return results;
    }

    @Override
    public void addSlot(TimeslotDTO dto) {
        Stylist stylist = stylistRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Stylist not found"));

        Timeslot slot = new Timeslot();
        slot.setStylist(stylist);
        slot.setDayOfWeek(DayOfWeek.valueOf(dto.getDayOfWeek()));
        slot.setTime(LocalTime.parse(dto.getStartTime()));

        timeslotRepository.save(slot);
    }

    @Override
    public void addDefaultSlots(TimeslotDTO dto) {
        Stylist stylist = stylistRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Stylist not found"));

        DayOfWeek day = DayOfWeek.valueOf(dto.getDayOfWeek());

        for (int hour = 9; hour < 16; hour++) {
            Timeslot slot = new Timeslot();
            slot.setStylist(stylist);
            slot.setDayOfWeek(day);
            slot.setTime(LocalTime.of(hour, 0));

            timeslotRepository.save(slot);
        }
    }

}
