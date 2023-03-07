package com.erickrodrigues.musicflux.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "profiles")
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @Operation(summary = "Log in")
    @PostMapping("/login")
    public ProfileDetailsDto login(@RequestBody @Valid LoginDto loginDto) {
        var profile = profileService.login(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        return profileMapper.toProfileDetailsDto(profile);
    }

    @Operation(summary = "Create a new profile")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileDetailsDto createProfile(@RequestBody @Valid CreateProfileDto createProfileDto) {
        var profile = profileService.signUp(
                createProfileDto.getName(),
                createProfileDto.getUsername(),
                createProfileDto.getEmail(),
                createProfileDto.getPassword()
        );

        return profileMapper.toProfileDetailsDto(profile);
    }
}
