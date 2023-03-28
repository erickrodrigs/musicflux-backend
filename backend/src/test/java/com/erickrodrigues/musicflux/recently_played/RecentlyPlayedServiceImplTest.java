package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecentlyPlayedServiceImplTest {

    private static final String WRONG_NUMBER_OF_RECENTLY_PLAYED_TRACKS = "Wrong number of recently played tracks";
    private static final String WRONG_TRACK = "Wrong track";
    private static final String WRONG_USER = "Wrong user";

    @Mock
    private RecentlyPlayedRepository recentlyPlayedRepository;

    @InjectMocks
    private RecentlyPlayedServiceImpl recentlyPlayedService;

    @Test
    public void shouldFindAllRecentlyPlayedTracksByUserId() {
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        final RecentlyPlayed recentlyPlayed1 = RecentlyPlayed.builder()
                .id(1L)
                .track(Track.builder().build())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        final RecentlyPlayed recentlyPlayed2 = RecentlyPlayed.builder()
                .id(2L)
                .track(Track.builder().build())
                .user(user)
                .createdAt(LocalDateTime.now().plusDays(2))
                .build();
        final RecentlyPlayed recentlyPlayed3 = RecentlyPlayed.builder()
                .id(3L)
                .track(Track.builder().build())
                .user(user)
                .createdAt(LocalDateTime.now().plusDays(4))
                .build();
        final Pageable pageable = PageRequest.of(0, 1);
        when(recentlyPlayedRepository.findAllByUserIdOrderByCreatedAtDesc(pageable, userId)).thenReturn(
                new PageImpl<>(List.of(recentlyPlayed3, recentlyPlayed2, recentlyPlayed1))
        );

        final Page<RecentlyPlayed> page = recentlyPlayedService.findAllByUserId(pageable, userId);

        assertEquals(3, page.toList().size(), WRONG_NUMBER_OF_RECENTLY_PLAYED_TRACKS);
        assertTrue(page.toList().contains(recentlyPlayed3));
        verify(recentlyPlayedRepository, times(1)).findAllByUserIdOrderByCreatedAtDesc(eq(pageable), eq(userId));
    }

    @Test
    public void shouldSaveARecentlyPlayedTrack() {
        final Track track = Track.builder().build();
        final User user = User.builder().build();
        final RecentlyPlayed recentlyPlayed = RecentlyPlayed
                .builder()
                .track(track)
                .user(user)
                .build();
        when(recentlyPlayedRepository.save(recentlyPlayed)).thenReturn(recentlyPlayed);

        final RecentlyPlayed actualRecentlyPlayed = recentlyPlayedService.save(track, user);

        assertNotNull(actualRecentlyPlayed);
        assertEquals(track, actualRecentlyPlayed.getTrack(), WRONG_TRACK);
        assertEquals(user, actualRecentlyPlayed.getUser(), WRONG_USER);
        verify(recentlyPlayedRepository, times(1)).save(recentlyPlayed);
    }
}
