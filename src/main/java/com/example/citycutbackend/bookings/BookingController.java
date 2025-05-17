package com.example.citycutbackend.bookings;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/booking")
public class BookingController {
    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping("/")
    public ResponseEntity<?> postBooking(@RequestBody BookingDTO dto) {
        try {
            Booking booking = bookingService.createBooking(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        }catch(RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
