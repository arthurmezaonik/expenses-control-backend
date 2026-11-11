package com.home.expenses_control.model;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

public class AccountSecurity extends User {
    private final UUID uid;
    private final String firstName;
    private final String lastName;

    public AccountSecurity(
        UUID uid,
        String firstName,
        String lastName,
        String email,
        String password,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(email, password, authorities);
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
