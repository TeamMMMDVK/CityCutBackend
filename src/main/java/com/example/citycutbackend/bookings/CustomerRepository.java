package com.example.citycutbackend.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @NativeQuery(value = "SELECT id FROM customers WHERE user_id=?1")
    Integer findCustomerByUserID(int userid);
}
