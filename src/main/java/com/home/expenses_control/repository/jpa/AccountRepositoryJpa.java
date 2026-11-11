package com.home.expenses_control.repository.jpa;

import com.home.expenses_control.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryJpa extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);
}
