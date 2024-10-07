package com.example.rhbapp.service;

import com.example.rhbapp.dto.CustomerDto;
import com.example.rhbapp.dto.CustomerSearchDto;
import com.example.rhbapp.entity.Customer;
import com.example.rhbapp.exception.ResourceNotFoundException;
import com.example.rhbapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDto customerDto;
    private Customer customer;

    @BeforeEach
    void setup() {
        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("Jahid Hasan");
        customerDto.setEmail("jahid.hasan@gmail.com");

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Jahid Hasan");
        customer.setEmail("jahid.hasan@gmail.com");
    }

    @Test
    void testGetAllCustomers_withSearchCriteria() {
        Pageable pageable = Pageable.unpaged();
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));
        when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(customerPage);

        Page<CustomerDto> result = customerService.getAllCustomers(new CustomerSearchDto(), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Jahid Hasan", result.getContent().get(0).getName());
        verify(customerRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(1L);

        assertEquals("Jahid Hasan", result.getName());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void testSaveCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.saveCustomer(customerDto);

        assertEquals("Jahid Hasan", result.getName());
        assertEquals(1L, result.getId());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto updatedCustomer = new CustomerDto();
        customerDto.setName("Rahul");
        customerDto.setEmail("rahul@gmail.com");

        Customer newCustomer = new Customer();
        newCustomer.setName("Rahul");
        newCustomer.setEmail("rahul@gmail.com");

        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        CustomerDto result = customerService.updateCustomer(1L, updatedCustomer);

        assertEquals("Rahul", result.getName());
        assertEquals("rahul@gmail.com", result.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(1L, customerDto));
    }

    @Test
    void testDeleteCustomer() {
        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(1L);
    }

}