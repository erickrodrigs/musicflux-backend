package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.RecentlyPlayedRepository;
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

    @Mock
    private RecentlyPlayedRepository recentlyPlayedRepository;

    @InjectMocks
    private RecentlyPlayedServiceImpl recentlyPlayedService;

    @Test
    public void findAllByProfileId() {
        final Long profileId = 1L;
        final Profile profile = Profile.builder().id(profileId).build();
        final RecentlyPlayed recentlyPlayed1 = RecentlyPlayed.builder()
                .id(1L)
                .song(Song.builder().build())
                .profile(profile)
                .createdAt(LocalDateTime.now())
                .build();
        final RecentlyPlayed recentlyPlayed2 = RecentlyPlayed.builder()
                .id(2L)
                .song(Song.builder().build())
                .profile(profile)
                .createdAt(LocalDateTime.now().plusDays(2))
                .build();
        final RecentlyPlayed recentlyPlayed3 = RecentlyPlayed.builder()
                .id(3L)
                .song(Song.builder().build())
                .profile(profile)
                .createdAt(LocalDateTime.now().plusDays(4))
                .build();

        final Pageable pageable = PageRequest.of(0, 1);
        when(recentlyPlayedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId)).thenReturn(
                new PageImpl<>(List.of(recentlyPlayed3, recentlyPlayed2, recentlyPlayed1))
        );

        final Page<RecentlyPlayed> page = recentlyPlayedService.findAllByProfileId(pageable, profileId);

        assertEquals(3, page.toList().size());
        assertTrue(page.toList().contains(recentlyPlayed3));

        verify(recentlyPlayedRepository, times(1)).findAllByProfileIdOrderByCreatedAtDesc(any(), eq(profileId));
    }
}
