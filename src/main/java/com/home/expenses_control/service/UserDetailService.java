package com.home.expenses_control.service;

import com.home.expenses_control.model.AccountSecurity;
import com.home.expenses_control.repository.AccountRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        var account = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));
        return new AccountSecurity(
            account.getId(),
            account.getFirstName(),
            account.getLastName(),
            account.getEmail(),
            account.getPasswordHash(),
            List.of(new SimpleGrantedAuthority("AUTHENTICATED"))
        );
    }
}
