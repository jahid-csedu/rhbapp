package com.example.rhbapp.service;

import com.example.rhbapp.dto.AccountDto;
import com.example.rhbapp.dto.AccountSearchDto;
import com.example.rhbapp.dto.CustomerDto;
import com.example.rhbapp.entity.Account;
import com.example.rhbapp.entity.Customer;
import com.example.rhbapp.exception.ResourceNotFoundException;
import com.example.rhbapp.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AccountService accountService;

    private Account account;
    private AccountDto accountDto;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Jahid Hasan");
        customer.setEmail("jahid.hasan@gmail.com");

        account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456789");
        account.setBalance(BigDecimal.valueOf(1000.0));
        account.setCustomer(customer);

        accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());
        accountDto.setCustomerId(customer.getId());
    }

    @Test
    void testGetAllAccounts() {
        AccountSearchDto searchDto = new AccountSearchDto();
        searchDto.setAccountNumber(account.getAccountNumber());
        Pageable pageable = Pageable.unpaged();
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.findBy(any(), any(), eq(pageable))).thenReturn(accountPage);

        Page<AccountDto> result = accountService.getAllAccounts(searchDto, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("123456789", result.getContent().get(0).getAccountNumber());
        verify(accountRepository).findBy(searchDto.getAccountNumber(), searchDto.getCustomerId(), pageable);
    }

    @Test
    void testGetAccountById_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountDto result = accountService.getAccountById(1L);

        assertEquals("123456789", result.getAccountNumber());
    }

    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testSaveAccount() {
        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        when(customerService.getCustomerById(1L)).thenReturn(customer);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDto result = accountService.saveAccount(accountDto);

        assertEquals("123456789", result.getAccountNumber());
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("987654321");
        accountDto.setBalance(BigDecimal.valueOf(1500.0));

        Account updatedAccount = account;
        updatedAccount.setAccountNumber("987654321");
        updatedAccount.setBalance(BigDecimal.valueOf(1500.0));

        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        AccountDto result = accountService.updateAccount(1L, accountDto);

        assertEquals("987654321", result.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1500.0), result.getBalance());
    }

    @Test
    void testUpdateAccount_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.updateAccount(1L, new AccountDto()));
    }

    @Test
    void testDeleteAccount() {
        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

}