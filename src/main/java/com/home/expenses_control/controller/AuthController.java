package com.home.expenses_control.controller;

import com.home.expenses_control.dto.request.LoginRequestDto;
import com.home.expenses_control.dto.response.CurrentAccountResponseDto;
import com.home.expenses_control.dto.response.LoginResponseDto;
import com.home.expenses_control.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/me")
    public CurrentAccountResponseDto getCurrentAccount(@AuthenticationPrincipal Jwt jwt) {
        return new CurrentAccountResponseDto(
            UUID.fromString(jwt.getClaimAsString("account_id")),
            jwt.getClaimAsString("first_name"),
            jwt.getClaimAsString("last_name"),
            jwt.getSubject()
        );
    }
}
