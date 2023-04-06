package com.erickrodrigues.musicflux.user;

import com.erickrodrigues.musicflux.shared.ResourceAlreadyExistsException;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String WRONG_USER_ID = "Wrong user ID";
    private static final String WRONG_USERNAME = "Wrong username";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(passwordEncoder.encode(password)).thenReturn(password);

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

    @Test
    public void shouldUpdateSpecifiedUserInfo() {
        final Long userId = 1L;
        final User user = User.builder()
                .id(userId)
                .name("Erick")
                .username("erick123")
                .email("erick@erick.com")
                .password("erick123")
                .build();
        final Map<String, Object> updates = Map.of(
                "name", "Carlos",
                "email", "carlos@carlos.com",
                "password", "carlos123"
        );
        final User userWithUpdatedInfo = User.builder()
                .id(user.getId())
                .name((String) updates.get("name"))
                .username(user.getUsername())
                .email((String) updates.get("email"))
                .password((String) updates.get("password"))
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(eq(userWithUpdatedInfo))).thenReturn(userWithUpdatedInfo);
        when(passwordEncoder.encode((String) updates.get("password"))).thenReturn((String) updates.get("password"));

        final User actualUser = userService.update(userId, updates);

        assertNotNull(actualUser);
        assertEquals(userWithUpdatedInfo.getId(), actualUser.getId());
        assertEquals(userWithUpdatedInfo.getName(), actualUser.getName());
        assertEquals(userWithUpdatedInfo.getUsername(), actualUser.getUsername());
        assertEquals(userWithUpdatedInfo.getEmail(), actualUser.getEmail());
        assertEquals(userWithUpdatedInfo.getPassword(), actualUser.getPassword());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(eq(userWithUpdatedInfo));
        verify(passwordEncoder, times(1)).encode(userWithUpdatedInfo.getPassword());
    }

    @Test
    public void shouldThrowAnExceptionWhenUpdatingEmailToOneThatAlreadyExists() {
        final Long userId = 1L;
        final User user = User.builder()
                .id(userId)
                .name("Erick")
                .username("erick123")
                .email("erick@erick.com")
                .password("erick123")
                .build();
        final Map<String, Object> updates = Map.of(
                "name", "Carlos",
                "email", "carlos@carlos.com",
                "password", "carlos123"
        );
        final User userWithSameEmail = User.builder()
                .id(user.getId())
                .name((String) updates.get("name"))
                .username(user.getUsername())
                .email((String) updates.get("email"))
                .password((String) updates.get("password"))
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameOrEmail("", userWithSameEmail.getEmail()))
                .thenReturn(Optional.of(userWithSameEmail));

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.update(userId, updates));
        verify(userRepository, never()).save(any());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).findByUsernameOrEmail(eq(""), eq(userWithSameEmail.getEmail()));
    }

    @Test
    public void shouldThrowAnExceptionWhenUpdatingUsernameToOneThatAlreadyExists() {
        final Long userId = 1L;
        final User user = User.builder()
                .id(userId)
                .name("Erick")
                .username("erick123")
                .email("erick@erick.com")
                .password("erick123")
                .build();
        final Map<String, Object> updates = Map.of(
                "name", "Carlos",
                "username", "carlos123",
                "password", "carlos123"
        );
        final User userWithSameEmail = User.builder()
                .id(user.getId())
                .name((String) updates.get("name"))
                .username((String) updates.get("username"))
                .email(user.getEmail())
                .password((String) updates.get("password"))
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameOrEmail(userWithSameEmail.getUsername(), ""))
                .thenReturn(Optional.of(userWithSameEmail));

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.update(userId, updates));
        verify(userRepository, never()).save(any());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).findByUsernameOrEmail(eq(userWithSameEmail.getUsername()), eq(""));
    }
}
