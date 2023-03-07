package com.erickrodrigues.musicflux.profile;

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
public class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    public void login() {
        final Profile profile = Profile.builder().id(1L).username("erick666").password("scooby-doo").build();

        when(profileRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(profile));

        final Profile actualProfile = profileService.login(profile.getUsername(), profile.getPassword());

        assertNotNull(actualProfile);
        assertEquals(profile.getId(), actualProfile.getId());
        verify(profileRepository, times(1)).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void loginWhenUsernameOrPasswordDoNotExist() {
        when(profileRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profileService.login("erick666", "scooby-doo"));
        verify(profileRepository, times(1)).findByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void signUp() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final Profile profile = Profile.builder()
                .id(1L)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();

        when(profileRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        when(profileRepository.save(any())).thenReturn(profile);

        final Profile actualProfile = profileService.signUp(name, username, email, password);

        assertNotNull(actualProfile);
        assertEquals(profile.getId(), actualProfile.getId());
        verify(profileRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(profileRepository, times(1)).save(any());
    }

    @Test
    public void signUpWithUsernameOrEmailThatAlreadyExist() {
        final String name = "Erick", username = "erick666", email = "erick@erick.com", password = "scooby-doo";
        final Profile profile = Profile.builder()
                .id(1L)
                .name(name)
                .username(username)
                .email(email)
                .password(password)
                .build();

        when(profileRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(profile));

        assertThrows(RuntimeException.class, () -> profileService.signUp(name, username, email, password));
        verify(profileRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(profileRepository, times(0)).save(any());
    }
}
