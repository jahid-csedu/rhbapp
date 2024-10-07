package com.example.rhbapp.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateDto {
    @JsonAlias("currency_pair")
    private String currencyPair;
    @JsonAlias("exchange_rate")
    private double exchangeRate;
}
