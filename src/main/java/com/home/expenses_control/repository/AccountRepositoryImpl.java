package com.home.expenses_control.repository;

import com.home.expenses_control.exceptions.AccountNotFoundException;
import com.home.expenses_control.model.Account;
import com.home.expenses_control.repository.jpa.AccountRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountRepositoryJpa accountRepository;

    public AccountRepositoryImpl(AccountRepositoryJpa accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public boolean hasAccounts() {
        return accountRepository.count() > 0;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    private Account getAccountModel(Optional<Account> accountModel) {
        return accountModel.orElseThrow(AccountNotFoundException::new);
    }
}
