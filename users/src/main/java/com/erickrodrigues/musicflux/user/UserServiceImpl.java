package com.erickrodrigues.musicflux.user;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.BaseService;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with that ID does not exist"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findByUsernameOrEmail(username, "")
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
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
                        .password(passwordEncoder.encode(password))
                        .build()
        );
    }

    @Override
    public User update(Long id, Map<String, Object> updates) {
        final User user = findById(id);
        final Map<String, Consumer<String>> updatesConsumerMap = Map.of(
                "name", user::setName,
                "username", (username) -> {
                    userRepository.findByUsernameOrEmail(username, "")
                            .ifPresent((s) -> {
                                throw new ResourceAlreadyExistsException("User with that email already exists");
                            });
                    user.setUsername(username);
                },
                "email", (email) -> {
                    userRepository.findByUsernameOrEmail("", email)
                            .ifPresent((s) -> {
                                throw new ResourceAlreadyExistsException("User with that email already exists");
                            });
                    user.setEmail(email);
                },
                "password", (password) -> user.setPassword(passwordEncoder.encode(password))
        );

        updatesConsumerMap.forEach((key, consumer) -> {
            if (!updates.containsKey(key) || Objects.isNull(updates.get(key))) return;

            consumer.accept((String) updates.get(key));
        });

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username);
    }
}
