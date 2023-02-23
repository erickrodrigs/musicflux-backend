package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProfileServiceImpl extends BaseService implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile login(String username, String password) {
        final Optional<Profile> optionalProfile = profileRepository.findByUsernameAndPassword(username, password);

        if (optionalProfile.isEmpty()) {
            throw new RuntimeException("Username or password invalid");
        }

        return optionalProfile.get();
    }

    @Transactional
    @Override
    public Profile signUp(String name, String username, String email, String password) {
        if (profileRepository.findByUsernameOrEmail(username, email).isPresent()) {
            throw new RuntimeException("Username or email already exist");
        }

        return profileRepository.save(Profile.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build()
        );
    }
}
