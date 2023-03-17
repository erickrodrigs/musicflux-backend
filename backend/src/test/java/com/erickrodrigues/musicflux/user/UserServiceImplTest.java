package com.erickrodrigues.musicflux.user;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String WRONG_USER_ID = "Wrong user ID";
    private static final String WRONG_USERNAME = "Wrong username";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldFindUserByTheirId() {
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final User actualUser = userService.findById(userId);

        assertNotNull(actualUser);
        assertEquals(userId, actualUser.getId(), WRONG_USER_ID);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingUserWithInvalidId() {
        final Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void shouldFindUserByTheirUsername() {
        final String username = "erickrodrigs";
        final User user = User.builder().username(username).build();
        when(userRepository.findByUsernameOrEmail(eq(username), anyString())).thenReturn(Optional.of(user));

        final User actualUser = userService.findByUsername(username);

        assertNotNull(actualUser);
        assertEquals(username, actualUser.getUsername(), WRONG_USERNAME);
        verify(userRepository, times(1)).findByUsernameOrEmail(eq(username), anyString());
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingUserWithInvalidUsername() {
        final String username = "erickrodrigs";
        when(userRepository.findByUsernameOrEmail(eq(username), anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername(username));
        verify(userRepository, times(1)).findByUsernameOrEmail(eq(username), anyString());
    }

    @Test
    public void shouldRegisterANewUser() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final User user = User.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();
        when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        final User actualUser = userService.register(name, username, email, password);

        assertNotNull(actualUser);
        assertEquals(user.getUsername(), actualUser.getUsername(), WRONG_USERNAME);
        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldThrowAnExceptionWhenRegisteringANewUserWithUsernameOrEmailThatAlreadyExist() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final User user = User.builder()
                .id(1L)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();
        when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.register(name, username, email, password));
        verify(userRepository, times(1)).findByUsernameOrEmail(username, email);
        verify(userRepository, times(0)).save(user);
    }
}
