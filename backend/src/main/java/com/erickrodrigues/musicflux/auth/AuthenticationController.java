package com.erickrodrigues.musicflux.auth;

import com.erickrodrigues.musicflux.profile.CreateProfileDto;
import com.erickrodrigues.musicflux.profile.LoginDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthTokenDto register(@RequestBody @Valid CreateProfileDto createProfileDto) {
        return AuthTokenDto
                .builder()
                .token(authenticationService.register(
                        createProfileDto.getName(),
                        createProfileDto.getUsername(),
                        createProfileDto.getEmail(),
                        createProfileDto.getPassword()
                ))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public AuthTokenDto authenticate(@RequestBody @Valid LoginDto loginDto) {
        return AuthTokenDto
                .builder()
                .token(authenticationService.authenticate(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                ))
                .build();
    }
}
