package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.dtos.CreateProfileDto;
import com.erickrodrigues.musicflux.dtos.LoginDto;
import com.erickrodrigues.musicflux.dtos.ProfileDetailsDto;
import com.erickrodrigues.musicflux.mappers.ProfileMapper;
import com.erickrodrigues.musicflux.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @PostMapping("/login")
    public ProfileDetailsDto login(@RequestBody @Valid LoginDto loginDto) {
        var profile = profileService.login(
                loginDto.getUsername(),
                loginDto.getPassword()
        );

        return profileMapper.toProfileDetailsDto(profile);
    }

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
