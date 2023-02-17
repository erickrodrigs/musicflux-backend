package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.User;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import com.erickrodrigues.musicflux.repositories.UserRepository;
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

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void login() {
        final User user = User.builder().id(1L).username("erick666").password("scooby-doo").build();
        final Profile profile = Profile.builder().id(1L).build();

        user.setProfile(profile);
        profile.setUser(user);

        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(user));

        final Profile actualProfile = userService.login(user.getUsername(), user.getPassword());

        assertNotNull(actualProfile);
        assertEquals(profile.getId(), actualProfile.getId());
        assertEquals(profile.getUser().getId(), actualProfile.getUser().getId());
        verify(userRepository, times(1)).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void loginWhenUsernameOrPasswordDoNotExist() {
        when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.login("erick666", "scooby-doo"));
        verify(userRepository, times(1)).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void signUp() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final Profile profile = Profile.builder().build();
        final User user = User.builder()
                .id(1L)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .profile(profile)
                .build();

        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        final User actualUser = userService.signUp(name, username, email, password);

        assertNotNull(actualUser);
        assertNotNull(actualUser.getProfile());
        assertEquals(user.getId(), actualUser.getId());
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(userRepository, times(1)).save(any());
        verify(profileRepository, times(1)).save(any());
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

        assertThrows(RuntimeException.class, () -> userService.signUp(name, username, email, password));
        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(userRepository, times(0)).save(any());
        verify(profileRepository, times(0)).save(any());
    }
}
