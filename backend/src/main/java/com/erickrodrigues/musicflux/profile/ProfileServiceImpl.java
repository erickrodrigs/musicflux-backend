package com.erickrodrigues.musicflux.profile;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl extends BaseService implements ProfileService, UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public Profile findByUsername(String username) {
        return profileRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    @Transactional
    @Override
    public Profile register(String name, String username, String email, String password) {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }
}
