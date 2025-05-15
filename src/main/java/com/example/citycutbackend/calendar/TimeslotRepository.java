package com.example.citycutbackend.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Integer> {

    @Query("SELECT t FROM timeslots t WHERE t.date = :date AND t.isAvailable = true ORDER BY t.time ASC")
    List<Timeslot> fetchAllAvailableTimeslotsForDay(@Param("date") LocalDate date);
}
