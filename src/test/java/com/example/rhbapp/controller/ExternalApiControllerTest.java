package com.example.rhbapp.controller;

import com.example.rhbapp.dto.ExchangeRateDto;
import com.example.rhbapp.service.ExternalApiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExternalApiController.class)
class ExternalApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExternalApiService externalApiService;

    @Test
    void callExchangeRateApi_ShouldReturnExchangeRate() throws Exception {
        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setCurrencyPair("GBP_AUD");
        exchangeRateDto.setExchangeRate(1.911863);

        Mockito.when(externalApiService.callExchangeRateApi(anyString()))
                .thenReturn(exchangeRateDto);

        mockMvc.perform(get("/external-api/exchange-rate")
                        .param("exchangePair", "GBP_AUD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyPair", is("GBP_AUD")))
                .andExpect(jsonPath("$.exchangeRate", is(1.911863)));
    }
}