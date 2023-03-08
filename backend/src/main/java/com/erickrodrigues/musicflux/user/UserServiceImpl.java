package com.erickrodrigues.musicflux.user;

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
public class UserServiceImpl extends BaseService implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    @Transactional
    @Override
    public User register(String name, String username, String email, String password) {
        userRepository
                .findByUsernameOrEmail(username, email)
                .ifPresent((s) -> {
                    throw new ResourceAlreadyExistsException("Username or email already exist");
                });

        return userRepository.save(
                User.builder()
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
