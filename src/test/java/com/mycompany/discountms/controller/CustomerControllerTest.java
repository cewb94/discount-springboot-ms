package com.mycompany.discountms.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mycompany.discountms.entity.Customer;
import com.mycompany.discountms.enums.CustomerType;
import com.mycompany.discountms.services.CustomerService;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired MockMvc mvc;

    @MockBean CustomerService customerService;

    @Test
    void getAllCustomers_returns200() throws Exception {
        Customer c = new Customer("Test", CustomerType.LOYAL, LocalDate.now(), false);
        when(customerService.getAllCustomers()).thenReturn(List.of(c));

        mvc.perform(get("/api/customers"))
           .andExpect(status().isOk());

        verify(customerService).getAllCustomers();
    }

    @Test
    void getCustomerById_missing_returns404() throws Exception {
        when(customerService.getCustomerById(99L)).thenReturn(null);

        mvc.perform(get("/api/customers/99"))
           .andExpect(status().isNotFound());

        verify(customerService).getCustomerById(99L);
    }
}
