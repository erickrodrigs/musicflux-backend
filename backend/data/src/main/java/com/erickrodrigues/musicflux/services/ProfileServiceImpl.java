package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.exceptions.InvalidCredentialsException;
import com.erickrodrigues.musicflux.exceptions.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl extends BaseService implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile login(String username, String password) {
        return profileRepository
                .findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new InvalidCredentialsException("Username or password invalid"));
    }

    @Transactional
    @Override
    public Profile signUp(String name, String username, String email, String password) {
        profileRepository
                .findByUsernameOrEmail(username, email)
                .ifPresent((s) -> {
                    throw new ResourceAlreadyExistsException("Username or email already exist");
                });

        return profileRepository.save(
                Profile.builder()
                        .name(name)
                        .username(username)
                        .email(email)
                        .password(password)
                        .build()
        );
    }
}
