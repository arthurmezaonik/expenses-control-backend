package com.home.expenses_control.repository;

import com.home.expenses_control.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByEmail(String email);
    boolean hasAccounts();
    Account save(Account account);
}
