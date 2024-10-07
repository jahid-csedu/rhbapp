package com.example.rhbapp.integration;

import com.example.rhbapp.dto.AccountDto;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/accounts.sql")
class AccountApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String BASE_URL = "/accounts";
    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testGetAllAccounts() {
        String url = BASE_URL + "/search?page=0&size=5";

        ResponseEntity<PageResponseDto<AccountDto>> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PageResponseDto<AccountDto> pageResponse = response.getBody();
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.totalRecords()).isPositive();
        assertThat(pageResponse.data()).isNotEmpty();
    }

    @Test
    void testGetAccountById() {
        String url = BASE_URL + "/3";

        ResponseEntity<AccountDto> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AccountDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AccountDto account = response.getBody();
        assertThat(account).isNotNull();
        assertThat(account.getId()).isEqualTo(3L);
        assertThat(account.getAccountNumber()).isEqualTo("1234567890");
    }

    @Test
    void testCreateAccount() {
        AccountDto newAccount = new AccountDto();
        newAccount.setAccountNumber("2233445566");
        newAccount.setBalance(BigDecimal.valueOf(5000.0));
        newAccount.setCustomerId(1L);

        HttpEntity<AccountDto> request = new HttpEntity<>(newAccount, headers);
        ResponseEntity<AccountDto> response = testRestTemplate.postForEntity(BASE_URL, request, AccountDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AccountDto createdAccount = response.getBody();
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getAccountNumber()).isEqualTo("2233445566");
        assertThat(createdAccount.getBalance()).isEqualTo(BigDecimal.valueOf(5000.0));
    }

    @Test
    void testUpdateAccount() {
        String url = BASE_URL + "/3";
        AccountDto updatedAccount = new AccountDto();
        updatedAccount.setAccountNumber("1111222233");
        updatedAccount.setBalance(BigDecimal.valueOf(7000.0));
        updatedAccount.setCustomerId(1L);

        HttpEntity<AccountDto> request = new HttpEntity<>(updatedAccount, headers);
        ResponseEntity<AccountDto> response = testRestTemplate.exchange(url, HttpMethod.PUT, request, AccountDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        AccountDto account = response.getBody();
        assertThat(account).isNotNull();
        assertThat(account.getAccountNumber()).isEqualTo("1111222233");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(7000.0));
    }

    @Test
    void testDeleteAccount() {
        String url = BASE_URL + "/3";

        ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
