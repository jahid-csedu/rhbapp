package com.example.rhbapp.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountDto {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Long customerId;
    private LocalDateTime createDate;
    private LocalDateTime lastModifyDate;
}
