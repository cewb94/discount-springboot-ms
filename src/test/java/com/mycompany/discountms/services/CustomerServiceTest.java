package com.mycompany.discountms.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.discountms.entity.Customer;
import com.mycompany.discountms.enums.CustomerType;
import com.mycompany.discountms.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock CustomerRepository customerRepository;

    @InjectMocks CustomerService customerService;

    @Test
    void getAllCustomers_returnsRepoList() {
        Customer c1 = new Customer("A", CustomerType.EMPLOYEE, LocalDate.now(), false);
        Customer c2 = new Customer("B", CustomerType.LOYAL, LocalDate.now(), false);

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Customer> out = customerService.getAllCustomers();

        assertThat(out).hasSize(2);
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_found_returnsCustomer() {
        Customer c = new Customer("A", CustomerType.AFFILIATE, LocalDate.now(), false);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));

        Customer out = customerService.getCustomerById(1L);

        assertThat(out).isSameAs(c);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_missing_returnsNull() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Customer out = customerService.getCustomerById(1L);

        assertThat(out).isNull();
        verify(customerRepository).findById(1L);
    }
}
