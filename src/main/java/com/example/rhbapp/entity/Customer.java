package com.example.rhbapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;

    @Column(updatable = false)
    private LocalDateTime createDate;

    private LocalDateTime lastModifyDate;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        lastModifyDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifyDate = LocalDateTime.now();
    }
}
