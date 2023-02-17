package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyListened;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.repositories.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecentlyListenedServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

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

        profile.setRecentlyListenedSongs(Set.of(recentlyListened1, recentlyListened2, recentlyListened3));

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        Pageable pageable;
        Page<RecentlyListened> page;

        pageable = PageRequest.of(0, 1);
        page = recentlyListenedService.findAllByProfileId(pageable, profileId);

        assertEquals(1, page.toSet().size());
        assertTrue(page.toSet().contains(recentlyListened3));

        pageable = PageRequest.of(1, 1);
        page = recentlyListenedService.findAllByProfileId(pageable, profileId);

        assertEquals(1, page.toSet().size());
        assertTrue(page.toSet().contains(recentlyListened2));

        pageable = PageRequest.of(2, 1);
        page = recentlyListenedService.findAllByProfileId(pageable, profileId);

        assertEquals(1, page.toSet().size());
        assertTrue(page.toSet().contains(recentlyListened1));

        pageable = PageRequest.of(3, 1);
        page = recentlyListenedService.findAllByProfileId(pageable, profileId);

        assertTrue(page.isEmpty());

        verify(profileRepository, times(4)).findById(profileId);
    }

    @Test
    public void findAllByProfileIdWhenPageSizeIsGreaterThanTotalOfRecentlyListenedSongs() {
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

        profile.setRecentlyListenedSongs(Set.of(recentlyListened1, recentlyListened2));

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        Pageable pageable = PageRequest.of(0, 4);
        Page<RecentlyListened> page = recentlyListenedService.findAllByProfileId(pageable, profileId);

        assertEquals(2, page.toSet().size());
        assertTrue(page.toSet().containsAll(Set.of(recentlyListened1, recentlyListened2)));
        verify(profileRepository, times(1)).findById(profileId);
    }

    @Test
    public void findAllByProfileIdThatDoesNotExist() {
        when(profileRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> recentlyListenedService.findAllByProfileId(PageRequest.of(1, 1), 1L)
        );

        verify(profileRepository, times(1)).findById(anyLong());
    }
}
