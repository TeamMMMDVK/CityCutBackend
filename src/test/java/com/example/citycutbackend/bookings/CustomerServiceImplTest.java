package com.example.citycutbackend.bookings;

import com.example.citycutbackend.user.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void createCustomerUserSuccess() {
        // Arrange
        UserModel user = new UserModel();
        user.setId(123);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class); //Assertions for objekt der bruges som argument. CustomerRepository.save(argument)

        // Act
        customerService.createCustomerUser(user);

        // Assert
        verify(customerRepository).save(captor.capture());
        Customer savedCustomer = captor.getValue();
        assertEquals(user, savedCustomer.getUser());
        assertNull(savedCustomer.getBookings());
    }
}