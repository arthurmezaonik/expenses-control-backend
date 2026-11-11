package com.home.expenses_control.controller;

import com.home.expenses_control.dto.response.CsrfTokenResponseDto;
import com.home.expenses_control.dto.response.CurrentAccountResponseDto;
import com.home.expenses_control.model.AccountSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/csrf")
    public CsrfTokenResponseDto getCsrfToken(CsrfToken csrfToken) {
        return new CsrfTokenResponseDto(csrfToken.getHeaderName(), csrfToken.getToken());
    }

    @GetMapping("/me")
    public CurrentAccountResponseDto getCurrentAccount(
        @AuthenticationPrincipal AccountSecurity account
    ) {
        return new CurrentAccountResponseDto(
            account.getUid(),
            account.getFirstName(),
            account.getLastName(),
            account.getUsername()
        );
    }
}
