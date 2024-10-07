package com.example.rhbapp.controller;

import com.example.rhbapp.dto.AccountDto;
import com.example.rhbapp.dto.AccountSearchDto;
import com.example.rhbapp.dto.PageResponseDto;
import com.example.rhbapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<AccountDto>> getAllAccounts(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        AccountSearchDto searchDto = buildSearchDto(accountNumber, customerId);
        Pageable pageable = PageRequest.of(page, size);
        Page<AccountDto> accounts = accountService.getAllAccounts(searchDto, pageable);
        return new ResponseEntity<>(buildPaginatedResponse(accounts), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        return new ResponseEntity<>(accountService.getAccountById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto account) {
        AccountDto createdAccount = accountService.saveAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto account) {
        AccountDto updatedAccount = accountService.updateAccount(id, account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private AccountSearchDto buildSearchDto(String accountNumber, Long customerId) {
        return AccountSearchDto.builder()
                .accountNumber(accountNumber)
                .customerId(customerId)
                .build();
    }

    private PageResponseDto<AccountDto> buildPaginatedResponse(Page<AccountDto> page) {
        return new PageResponseDto<>(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getContent()
        );
    }
}
