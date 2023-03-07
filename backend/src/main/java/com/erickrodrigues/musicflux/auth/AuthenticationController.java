package com.erickrodrigues.musicflux.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new profile")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthTokenDto register(@RequestBody @Valid AuthRegistrationDto authRegistrationDto) {
        return AuthTokenDto
                .builder()
                .token(authenticationService.register(
                        authRegistrationDto.getName(),
                        authRegistrationDto.getUsername(),
                        authRegistrationDto.getEmail(),
                        authRegistrationDto.getPassword()
                ))
                .build();
    }

    @Operation(summary = "Authenticate as a valid profile")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public AuthTokenDto authenticate(@RequestBody @Valid AuthCredentialsDto authCredentialsDto) {
        return AuthTokenDto
                .builder()
                .token(authenticationService.authenticate(
                        authCredentialsDto.getUsername(),
                        authCredentialsDto.getPassword()
                ))
                .build();
    }
}
