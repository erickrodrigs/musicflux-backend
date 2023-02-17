package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.User;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public UserServiceImpl(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile login(String username, String password) {
        final Optional<User> optionalUser = userRepository.findByUsernameAndPassword(username, password);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Username or password invalid");
        }

        return optionalUser.get().getProfile();
    }

    @Transactional
    @Override
    public User signUp(String name, String username, String email, String password) {
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            throw new RuntimeException("Username or email already exist");
        }

        Profile profile = Profile.builder().build();
        User user = User.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();

        user.setProfile(profile);
        profile.setUser(user);

        profileRepository.save(profile);

        return userRepository.save(user);
    }
}
