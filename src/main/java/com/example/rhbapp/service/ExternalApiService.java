package com.example.rhbapp.service;

import com.example.rhbapp.dto.ExchangeRateDto;
import com.example.rhbapp.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExternalApiService {
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://api.api-ninjas.com/v1/exchangerate?pair=";
    @Value("${app.ninja.api.key}")
    private String API_KEY;

    public ExchangeRateDto callExchangeRateApi(String exchangePair) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ExchangeRateDto> response = restTemplate.exchange(API_URL + exchangePair, HttpMethod.GET, entity, ExchangeRateDto.class);
            return response.getBody();
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Error accessing external API");
        }
    }
}
