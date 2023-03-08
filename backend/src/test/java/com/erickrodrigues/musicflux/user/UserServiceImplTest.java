package com.erickrodrigues.musicflux.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void signUp() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final User user = User.builder()
                .id(1L)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        final User actualUser = userService.register(name, username, email, password);

        assertNotNull(actualUser);
        assertEquals(user.getId(), actualUser.getId());
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void signUpWithUsernameOrEmailThatAlreadyExist() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final User user = User.builder()
                .id(1L)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.register(name, username, email, password));
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(userRepository, times(0)).save(any());
    }
}
