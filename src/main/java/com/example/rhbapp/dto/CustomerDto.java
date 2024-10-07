package com.example.rhbapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createDate;
    private LocalDateTime lastModifyDate;
}
