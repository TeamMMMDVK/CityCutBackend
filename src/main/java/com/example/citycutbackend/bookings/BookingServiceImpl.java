package com.example.citycutbackend.bookings;

import com.example.citycutbackend.calendar.Stylist;
import com.example.citycutbackend.calendar.StylistRepository;
import com.example.citycutbackend.calendar.Timeslot;
import com.example.citycutbackend.calendar.TimeslotRepository;
import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final TimeslotRepository timeslotRepository;
    private final CustomerRepository customerRepository;
    private final TreatmentRepository treatmentRepository;
    private final StylistRepository stylistRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, TimeslotRepository timeslotRepository, CustomerRepository customerRepository, TreatmentRepository treatmentRepository, StylistRepository stylistRepository) {
        this.bookingRepository = bookingRepository;
        this.timeslotRepository = timeslotRepository;
        this.customerRepository = customerRepository;
        this.treatmentRepository = treatmentRepository;
        this.stylistRepository = stylistRepository;
    }

    @Override
    @Transactional
    public Booking createBooking(BookingDTO dto) {
        Booking booking = new Booking();
        //booking.setTimeslots(); //loop through ids to find the timeslots
        List<Timeslot> timeslots = timeslotRepository.findAllById(dto.getTimeslotIds());

        booking.setTimeslots(timeslots);

        List<Treatment> treatments = treatmentRepository.findAllById(dto.getTreatmentIds());
        //booking.setTreatments; //No treatments on Booking entity?
        booking.setTreatments(treatments);

        //booking.setCustomer(customer); //Find customer by ID
        int customerID = customerRepository.findCustomerByUserID(dto.getUserID());
        Optional<Customer> customerOptional = customerRepository.findById(customerID);
        if (customerOptional.isPresent()) {
            booking.setCustomer(customerOptional.get());
        } else {
            throw new NoSuchElementException("No customer found with id: "+customerID);
        }

        //booking.setStylist(); //Find Stylist by ID
        Optional<Stylist> stylistToFind = stylistRepository.findById(dto.getStylistID());
        if (stylistToFind.isPresent()) {
            Stylist stylist = stylistToFind.get();
            booking.setStylist(stylist);
        } else {
            throw new NoSuchElementException("No stylist found with id: "+dto.getStylistID());
        }

        booking.setComment(dto.getComment());
        Booking savedBooking = bookingRepository.save(booking);
        setTimeslotAvailabilityAndBookingID(timeslots, booking);

        return savedBooking;
    }
    @Transactional
    public void setTimeslotAvailabilityAndBookingID(List<Timeslot> slots, Booking booking) {
        for (Timeslot t : slots) {
            t.setBooking(booking);
            t.setAvailable(false);
        }
        timeslotRepository.saveAll(slots);
    }
}
