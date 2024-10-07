package com.example.rhbapp.controller;


import com.example.rhbapp.dto.CustomerDto;
import com.example.rhbapp.dto.CustomerSearchDto;
import com.example.rhbapp.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private CustomerDto customerDto;

    @BeforeEach
    void setup() {
        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("Jahid Hasan");
        customerDto.setEmail("jahid.hasan@gmail.com");
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers() throws Exception {
        Page<CustomerDto> page = new PageImpl<>(Collections.singletonList(customerDto));
        Mockito.when(customerService.getAllCustomers(any(CustomerSearchDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/customers/search")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalRecords", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Jahid Hasan")))
                .andExpect(jsonPath("$.data[0].email", is("jahid.hasan@gmail.com")));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() throws Exception {
        Mockito.when(customerService.getCustomerById(1L)).thenReturn(customerDto);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jahid Hasan")))
                .andExpect(jsonPath("$.email", is("jahid.hasan@gmail.com")));
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        Mockito.when(customerService.saveCustomer(any(CustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Jahid Hasan")))
                .andExpect(jsonPath("$.email", is("jahid.hasan@gmail.com")));
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomer() throws Exception {
        Mockito.when(customerService.updateCustomer(eq(1L), any(CustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Jahid Hasan\", \"email\": \"jahid.hasan@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jahid Hasan")))
                .andExpect(jsonPath("$.email", is("jahid.hasan@gmail.com")));
    }

    @Test
    void deleteCustomer_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());
    }
}