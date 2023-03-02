package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyListened;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.RecentlyListenedRepository;
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
public class RecentlyListenedServiceImplTest {

    @Mock
    private RecentlyListenedRepository recentlyListenedRepository;

    @InjectMocks
    private RecentlyListenedServiceImpl recentlyListenedService;

    @Test
    public void findAllByProfileId() {
        final Long profileId = 1L;
        final Profile profile = Profile.builder().id(profileId).build();
        final RecentlyListened recentlyListened1 = RecentlyListened.builder()
                .id(1L)
                .song(Song.builder().build())
                .profile(profile)
                .createdAt(LocalDateTime.now())
                .build();
        final RecentlyListened recentlyListened2 = RecentlyListened.builder()
                .id(2L)
                .song(Song.builder().build())
                .profile(profile)
                .createdAt(LocalDateTime.now().plusDays(2))
                .build();
        final RecentlyListened recentlyListened3 = RecentlyListened.builder()
                .id(3L)
                .song(Song.builder().build())
                .profile(profile)
                .createdAt(LocalDateTime.now().plusDays(4))
                .build();

        final Pageable pageable = PageRequest.of(0, 1);
        when(recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId)).thenReturn(
                new PageImpl<>(List.of(recentlyListened3, recentlyListened2, recentlyListened1))
        );

        final Page<RecentlyListened> page = recentlyListenedService.findAllByProfileId(pageable, profileId);

        assertEquals(3, page.toList().size());
        assertTrue(page.toList().contains(recentlyListened3));

        verify(recentlyListenedRepository, times(1)).findAllByProfileIdOrderByCreatedAtDesc(any(), eq(profileId));
    }
}
