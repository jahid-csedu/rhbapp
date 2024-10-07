package com.example.rhbapp.integration;

import com.example.rhbapp.dto.CustomerDto;
import com.example.rhbapp.dto.PageResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/customers.sql")
class CustomerApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String BASE_URL = "/customers";
    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testGetAllCustomers() {
        String url = BASE_URL + "/search?page=0&size=5";

        ResponseEntity<PageResponseDto<CustomerDto>> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PageResponseDto<CustomerDto> pageResponse = response.getBody();
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.totalRecords()).isPositive();
        assertThat(pageResponse.data()).isNotEmpty();
    }

    @Test
    void testGetCustomerById() {
        String url = BASE_URL + "/3";

        ResponseEntity<CustomerDto> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CustomerDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomerDto customer = response.getBody();
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(3L);
        assertThat(customer.getName()).isEqualTo("Customer 1");
        assertThat(customer.getEmail()).isEqualTo("customer1@gmail.com");
    }

    @Test
    void testCreateCustomer() {
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setName("Customer");
        newCustomer.setEmail("customer@gmail.com");

        HttpEntity<CustomerDto> request = new HttpEntity<>(newCustomer, headers);
        ResponseEntity<CustomerDto> response = testRestTemplate.postForEntity(BASE_URL, request, CustomerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CustomerDto createdCustomer = response.getBody();
        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.getName()).isEqualTo("Customer");
        assertThat(createdCustomer.getEmail()).isEqualTo("customer@gmail.com");
    }

    @Test
    void testUpdateCustomer() {
        String url = BASE_URL + "/3";
        CustomerDto updatedCustomer = new CustomerDto();
        updatedCustomer.setName("Updated Customer");
        updatedCustomer.setEmail("updated.customer@gmail.com");

        HttpEntity<CustomerDto> request = new HttpEntity<>(updatedCustomer, headers);
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange(url, HttpMethod.PUT, request, CustomerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomerDto customer = response.getBody();
        assertThat(customer).isNotNull();
        assertThat(customer.getName()).isEqualTo("Updated Customer");
        assertThat(customer.getEmail()).isEqualTo("updated.customer@gmail.com");
    }

    @Test
    void testDeleteCustomer() {
        String url = BASE_URL + "/3";

        ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
