package com.home.expenses_control.config;

import com.home.expenses_control.model.Account;
import com.home.expenses_control.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialAccountSetup implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String rawPassword;

    public InitialAccountSetup(
        AccountRepository accountRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.initial-account.first-name:}") String firstName,
        @Value("${app.initial-account.last-name:}") String lastName,
        @Value("${app.initial-account.email:}") String email,
        @Value("${app.initial-account.password:}") String rawPassword
    ) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.rawPassword = rawPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (accountRepository.hasAccounts()) {
            return;
        }

        String normalizedFirstName = firstName.trim();
        String normalizedLastName = lastName.trim();
        String normalizedEmail = email.trim();

        if (normalizedFirstName.isEmpty() || normalizedLastName.isEmpty()
            || normalizedEmail.isEmpty() || rawPassword.isBlank()) {
            throw new IllegalStateException(
                "Configure APP_INITIAL_ACCOUNT_FIRST_NAME, APP_INITIAL_ACCOUNT_LAST_NAME, "
                    + "APP_INITIAL_ACCOUNT_EMAIL, and APP_INITIAL_ACCOUNT_PASSWORD "
                    + "to create the initial account."
            );
        }

        String passwordHash = passwordEncoder.encode(rawPassword);
        accountRepository.save(new Account(
            normalizedFirstName,
            normalizedLastName,
            normalizedEmail,
            passwordHash
        ));
    }
}
