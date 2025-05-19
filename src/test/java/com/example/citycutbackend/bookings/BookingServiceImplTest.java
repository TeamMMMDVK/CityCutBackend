package com.example.citycutbackend.bookings;

import com.example.citycutbackend.calendar.Stylist;
import com.example.citycutbackend.calendar.StylistRepository;
import com.example.citycutbackend.calendar.Timeslot;
import com.example.citycutbackend.calendar.TimeslotRepository;
import com.example.citycutbackend.treatments.Treatment;
import com.example.citycutbackend.treatments.TreatmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TimeslotRepository timeslotRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private StylistRepository stylistRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingSuccess() {
        //Arrange
        BookingDTO dto = new BookingDTO();
        dto.setTimeslotIds(List.of(1, 2));
        dto.setTreatmentIds(List.of(10));
        dto.setUserID(100);
        dto.setStylistID(200);
        dto.setComment("Test comment");

        Timeslot slot1 = new Timeslot();
        slot1.setId(1);
        Timeslot slot2 = new Timeslot();
        slot2.setId(2);

        Treatment treatment = new Treatment();
        treatment.setId(10);

        Customer customer = new Customer();
        customer.setId(1);
        Stylist stylist = new Stylist();
        stylist.setId(200);

        //Booking booking = new Booking();


        when(timeslotRepository.findAllById(List.of(1, 2))).thenReturn(List.of(slot1, slot2));
        when(treatmentRepository.findAllById(List.of(10))).thenReturn(List.of(treatment));
        when(customerRepository.findCustomerByUserID(100)).thenReturn(1);
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(stylistRepository.findById(200)).thenReturn(Optional.of(stylist));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(999);
            return b;
        });

        //Act
        Booking result = bookingService.createBooking(dto);

        //Assert
        assertNotNull(result);
        assertEquals(999, result.getId());
        assertEquals(customer, result.getCustomer());
        assertEquals(stylist, result.getStylist());
        assertEquals("Test comment", result.getComment());

        assertFalse(slot1.isAvailable());
        assertEquals(result, slot1.getBooking());
    }

    @Test
    void setTimeslotAvailabilityAndBookingID() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1);

        Timeslot t1 = new Timeslot();
        t1.setId(1);
        t1.setAvailable(true);
        Timeslot t2 = new Timeslot();
        t2.setId(2);
        t2.setAvailable(true);
        List<Timeslot> timeslots = List.of(t1, t2);

        // Act
        bookingService.setTimeslotAvailabilityAndBookingID(timeslots, booking);
        booking.setTimeslots(timeslots);

        // Assert
        for (Timeslot t : timeslots) {
            assertFalse(t.isAvailable());
            assertEquals(booking, t.getBooking());
        }

        verify(timeslotRepository).saveAll(timeslots);
        assertEquals(booking.getTimeslots().get(0).getBooking().getId(), booking.getId());
        assertEquals(booking.getTimeslots().get(1).getBooking().getId(), booking.getId());
        assertEquals(timeslots.get(0).isAvailable(), false);
        assertEquals(timeslots.get(1).isAvailable(), false);
    }
}