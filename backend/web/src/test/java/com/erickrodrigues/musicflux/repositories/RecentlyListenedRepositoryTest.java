package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.RecentlyListened;
import com.erickrodrigues.musicflux.domain.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RecentlyListenedRepositoryTest {

    @Autowired
    private RecentlyListenedRepository recentlyListenedRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SongRepository songRepository;

    final Long profileId = 1L;
    final Profile profile = Profile.builder().id(profileId).build();
    final Song song1 = Song.builder().id(1L).build();
    final Song song2 = Song.builder().id(2L).build();
    final Song song3 = Song.builder().id(3L).build();
    final RecentlyListened recentlyListened1 = RecentlyListened.builder()
            .id(1L)
            .song(song1)
            .profile(profile)
            .createdAt(LocalDateTime.now())
            .build();
    final RecentlyListened recentlyListened2 = RecentlyListened.builder()
            .id(2L)
            .song(song2)
            .profile(profile)
            .createdAt(LocalDateTime.now().plusDays(2))
            .build();
    final RecentlyListened recentlyListened3 = RecentlyListened.builder()
            .id(3L)
            .song(song3)
            .profile(profile)
            .createdAt(LocalDateTime.now().plusDays(4))
            .build();

    @BeforeEach
    public void setUp() {
        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);
        profileRepository.save(profile);
        recentlyListenedRepository.save(recentlyListened1);
        recentlyListenedRepository.save(recentlyListened2);
        recentlyListenedRepository.save(recentlyListened3);
    }

    @Test
    public void findAllByProfileIdOrderByCreatedAtDesc() {
        Pageable pageable;
        Page<RecentlyListened> page;

        pageable = PageRequest.of(0, 1);
        page = recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);

        assertEquals(1, page.toSet().size());
        assertTrue(page.toSet().contains(recentlyListened3));

        pageable = PageRequest.of(1, 1);
        page = recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);

        assertEquals(1, page.toSet().size());
        assertTrue(page.toSet().contains(recentlyListened2));

        pageable = PageRequest.of(2, 1);
        page = recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);

        assertEquals(1, page.toSet().size());
        assertTrue(page.toSet().contains(recentlyListened1));

        pageable = PageRequest.of(3, 1);
        page = recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);

        assertTrue(page.isEmpty());
    }

    @Test
    public void findAllByProfileIdOrderByCreatedAtDescWhenProfileDoesNotExist() {
        final Long unknownProfileId = 5L;
        final Pageable pageable = PageRequest.of(0, 3);
        final Page<RecentlyListened> page = recentlyListenedRepository.findAllByProfileIdOrderByCreatedAtDesc(
                pageable, unknownProfileId
        );

        assertTrue(page.isEmpty());
    }
}
