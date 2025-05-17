package com.example.citycutbackend.bookings;

import com.example.citycutbackend.user.UserModel;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createNewCustomer(Customer customer) {
        return null;
    }
    public void createCustomerUser(UserModel user) {
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setBookings(null);
        customerRepository.save(customer);
    }
    public int findCustomerByUserID(int userid) {
        return customerRepository.findCustomerByUserID(userid);
    }
}
