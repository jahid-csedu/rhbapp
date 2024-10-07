package com.example.rhbapp.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFound() {

        ResourceNotFoundException exception = new ResourceNotFoundException("Customer not found");


        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleResourceNotFound(exception);


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());


        ErrorResponse errorResponse = responseEntity.getBody();
        assert errorResponse != null;
        assertEquals("Customer not found", errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals(LocalDateTime.now().getYear(), errorResponse.getTimestamp().getYear()); // Timestamp is approximately equal
    }

}