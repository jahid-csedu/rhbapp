package com.example.rhbapp.controller;

import com.example.rhbapp.dto.ExchangeRateDto;
import com.example.rhbapp.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external-api")
@RequiredArgsConstructor
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    @GetMapping("/exchange-rate")
    public ResponseEntity<ExchangeRateDto> callGoogleApi(@RequestParam String exchangePair) {
        ExchangeRateDto response = externalApiService.callExchangeRateApi(exchangePair);
        return ResponseEntity.ok(response);
    }

}
