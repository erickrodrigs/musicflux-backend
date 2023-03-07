package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.profile.Profile;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.profile.ProfileRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RecentlyPlayedRepositoryTest {

    @Autowired
    private RecentlyPlayedRepository recentlyPlayedRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private SongRepository songRepository;

    final Long profileId = 1L;
    final Profile profile = Profile.builder().id(profileId).build();
    final Song song1 = Song.builder().id(1L).build();
    final Song song2 = Song.builder().id(2L).build();
    final Song song3 = Song.builder().id(3L).build();
    final RecentlyPlayed recentlyPlayed1 = RecentlyPlayed.builder()
            .id(1L)
            .song(song1)
            .profile(profile)
            .createdAt(LocalDateTime.now())
            .build();
    final RecentlyPlayed recentlyPlayed2 = RecentlyPlayed.builder()
            .id(2L)
            .song(song2)
            .profile(profile)
            .createdAt(LocalDateTime.now().plusDays(2))
            .build();
    final RecentlyPlayed recentlyPlayed3 = RecentlyPlayed.builder()
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
        recentlyPlayedRepository.save(recentlyPlayed1);
        recentlyPlayedRepository.save(recentlyPlayed2);
        recentlyPlayedRepository.save(recentlyPlayed3);
    }

    @Test
    public void findAllByProfileIdOrderByCreatedAtDesc() {
        Pageable pageable;
        Page<RecentlyPlayed> page;

        pageable = PageRequest.of(0, 3);
        page = recentlyPlayedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);

        assertEquals(3, page.toSet().size());
        assertTrue(page.toSet().containsAll(List.of(recentlyPlayed1, recentlyPlayed2, recentlyPlayed3)));

        pageable = PageRequest.of(1, 3);
        page = recentlyPlayedRepository.findAllByProfileIdOrderByCreatedAtDesc(pageable, profileId);

        assertTrue(page.isEmpty());
    }

    @Test
    public void findAllByProfileIdOrderByCreatedAtDescWhenProfileDoesNotExist() {
        final Long unknownProfileId = 5L;
        final Pageable pageable = PageRequest.of(0, 3);
        final Page<RecentlyPlayed> page = recentlyPlayedRepository.findAllByProfileIdOrderByCreatedAtDesc(
                pageable, unknownProfileId
        );

        assertTrue(page.isEmpty());
    }
}
