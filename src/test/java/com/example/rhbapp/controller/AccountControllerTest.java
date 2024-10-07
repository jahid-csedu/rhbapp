package com.example.rhbapp.controller;

import com.example.rhbapp.dto.AccountDto;
import com.example.rhbapp.dto.AccountSearchDto;
import com.example.rhbapp.service.AccountService;
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

import java.math.BigDecimal;
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

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;
    private AccountDto accountDto;

    @BeforeEach
    public void setUp() {
        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setAccountNumber("123456");
        accountDto.setBalance(BigDecimal.valueOf(1000.0));
        accountDto.setCustomerId(1L);
    }

    @Test
    void getAllAccounts_ShouldReturnAccounts() throws Exception {
        Page<AccountDto> page = new PageImpl<>(Collections.singletonList(accountDto));
        Mockito.when(accountService.getAllAccounts(any(AccountSearchDto.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/accounts/search")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalRecords", is(1)))
                .andExpect(jsonPath("$.data[0].accountNumber", is("123456")));
    }

    @Test
    void getAccountById_ShouldReturnAccount() throws Exception {
        Mockito.when(accountService.getAccountById(1L)).thenReturn(accountDto);

        mockMvc.perform(get("/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is("123456")))
                .andExpect(jsonPath("$.balance", is(1000.0)));
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        Mockito.when(accountService.saveAccount(any(AccountDto.class))).thenReturn(accountDto);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\": \"123456\", \"balance\": 1000.0, \"customerId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber", is("123456")))
                .andExpect(jsonPath("$.balance", is(1000.0)));
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() throws Exception {
        Mockito.when(accountService.updateAccount(eq(1L), any(AccountDto.class))).thenReturn(accountDto);

        mockMvc.perform(put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\": \"123456\", \"balance\": 1000.0, \"customerId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", is("123456")))
                .andExpect(jsonPath("$.balance", is(1000.0)));
    }

    @Test
    void deleteAccount_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());
    }

}