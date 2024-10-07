package com.example.rhbapp.repository;

import com.example.rhbapp.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    @Query("""
            from
            Account a
            where (:accountNumber is null or a.accountNumber = :accountNumber)
            and (:customerId is null or a.customer.id = :customerId)
            """)
    Page<Account> findBy(String accountNumber, Long customerId, Pageable pageable);
}
