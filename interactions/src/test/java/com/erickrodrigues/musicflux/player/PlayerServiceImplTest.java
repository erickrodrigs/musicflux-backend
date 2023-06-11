package com.erickrodrigues.musicflux.player;

import com.erickrodrigues.musicflux.recently_played.RecentlyPlayedService;
import com.erickrodrigues.musicflux.shared.ResourceNotFoundException;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackService;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private TrackService trackService;

    @Mock
    private RecentlyPlayedService recentlyPlayedService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    public void shouldPlayATrack() {
        final Long trackId = 1L, userId = 1L;
        final User user = User.builder().id(userId).build();
        final Track track = Track.builder().id(trackId).build();
        when(trackService.play(trackId)).thenReturn(track);
        when(userService.findById(userId)).thenReturn(user);

        playerService.play(userId, trackId);

        verify(trackService, times(1)).play(trackId);
        verify(userService, times(1)).findById(userId);
        verify(recentlyPlayedService, times(1)).save(track, user);
    }

    @Test
    public void shouldThrowAnExceptionWhenPlayingATrackWithInvalidId() {
        final Long invalidTrackId = 394L, userId = 1L;
        when(trackService.play(invalidTrackId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> playerService.play(userId, invalidTrackId));
        verify(trackService, times(1)).play(invalidTrackId);
    }

    @Test
    public void shouldThrowAnExceptionWhenUserWithInvalidIdPlaysATrack() {
        final Long trackId = 1L, invalidUserId = 394L;
        when(userService.findById(invalidUserId)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> playerService.play(invalidUserId, trackId));
        verify(userService, times(1)).findById(invalidUserId);
    }
}
