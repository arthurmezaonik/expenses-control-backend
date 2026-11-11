package com.home.expenses_control.service;

import com.home.expenses_control.dto.request.LoginRequestDto;
import com.home.expenses_control.dto.response.LoginResponseDto;
import com.home.expenses_control.model.AccountSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationMinutes;

    public AuthService(
        AuthenticationManager authenticationManager,
        JwtEncoder jwtEncoder,
        @Value("${app.jwt.issuer}") String issuer,
        @Value("${app.jwt.expiration-minutes:15}") long expirationMinutes
    ) {
        if (expirationMinutes <= 0) {
            throw new IllegalArgumentException("JWT expiration must be greater than zero minutes");
        }
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        var authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.email().trim(),
                loginRequest.password()
            )
        );
        AccountSecurity account = (AccountSecurity) authentication.getPrincipal();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(expirationMinutes * 60);
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer(issuer)
            .subject(account.getUsername())
            .issuedAt(issuedAt)
            .expiresAt(expiresAt)
            .claim("account_id", account.getUid().toString())
            .claim("first_name", account.getFirstName())
            .claim("last_name", account.getLastName())
            .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(),
            claims
        )).getTokenValue();

        return new LoginResponseDto(token, "Bearer", expiresAt);
    }
}
