package com.example.rhbapp.service;

import com.example.rhbapp.dto.AccountDto;
import com.example.rhbapp.dto.AccountSearchDto;
import com.example.rhbapp.entity.Account;
import com.example.rhbapp.entity.Customer;
import com.example.rhbapp.exception.ResourceNotFoundException;
import com.example.rhbapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;

    public Page<AccountDto> getAllAccounts(AccountSearchDto searchDto, Pageable pageable) {
        return accountRepository.findBy(searchDto.getAccountNumber(), searchDto.getCustomerId(), pageable)
                .map(this::convertToDto);
    }

    public AccountDto getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Account not Found"));
    }

    public AccountDto saveAccount(AccountDto accountDto) {
        Account account = convertToEntity(accountDto);
        Customer customer = getCustomer(accountDto);
        account.setCustomer(customer);
        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }

    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Optional<Account> existingAccountOptional = accountRepository.findById(id);

        if (existingAccountOptional.isPresent()) {
            Account account = existingAccountOptional.get();
            convertToUpdateEntity(accountDto, account);
            Account updatedAccount = accountRepository.save(account);
            return convertToDto(updatedAccount);
        }
        throw new ResourceNotFoundException("Account not Found");
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    private Customer getCustomer(AccountDto accountDto) {
        customerService.getCustomerById(accountDto.getCustomerId());
        Customer customer = new Customer();
        customer.setId(accountDto.getCustomerId());
        return customer;
    }

    private AccountDto convertToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setBalance(account.getBalance());
        accountDto.setCustomerId(account.getCustomer().getId());
        accountDto.setCreateDate(account.getCreateDate());
        accountDto.setLastModifyDate(account.getLastModifyDate());
        return accountDto;
    }

    private Account convertToEntity(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
        return account;
    }

    private void convertToUpdateEntity(AccountDto accountDto, Account account) {
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
    }
}
