package com.example.rhbapp.service;

import com.example.rhbapp.dto.ExchangeRateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalApiService externalApiService;

    @Test
    void testGetExchangeRate_Success() {

        ExchangeRateDto mockResponse = new ExchangeRateDto();
        mockResponse.setCurrencyPair("GBP_AUD");
        mockResponse.setExchangeRate(1.911863);

        when(restTemplate.exchange(eq("https://api.api-ninjas.com/v1/exchangerate?pair=GBP_AUD"),
                eq(HttpMethod.GET), any(HttpEntity.class), eq(ExchangeRateDto.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));


        ExchangeRateDto response = externalApiService.callExchangeRateApi("GBP_AUD");


        assertEquals("GBP_AUD", response.getCurrencyPair());
        assertEquals(1.911863, response.getExchangeRate());
    }

}