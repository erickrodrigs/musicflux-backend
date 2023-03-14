package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecentlyPlayedRepositoryTest {

    private static final String PAGE_IS_NOT_EMPTY = "Page is not empty";
    private static final String WRONG_PAGE_SIZE = "Wrong page size";
    private static final String PAGE_DOES_NOT_CONTAIN_SPECIFIED_RECENTLY_PLAYED_SONGS = "Page does not contain specified recently played songs";

    @Autowired
    private RecentlyPlayedRepository recentlyPlayedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    private static User user = User
            .builder()
            .build();
    private static Song song1 = Song
            .builder()
            .build();
    private static Song song2 = Song
            .builder()
            .build();
    private static Song song3 = Song
            .builder()
            .build();
    private static RecentlyPlayed recentlyPlayed1 = RecentlyPlayed
            .builder()
            .createdAt(LocalDateTime.now())
            .build();
    private static RecentlyPlayed recentlyPlayed2 = RecentlyPlayed
            .builder()
            .createdAt(LocalDateTime.now().plusDays(2))
            .build();
    private static RecentlyPlayed recentlyPlayed3 = RecentlyPlayed
            .builder()
            .createdAt(LocalDateTime.now().plusDays(4))
            .build();

    @BeforeAll
    public void setUp() {
        song1 = songRepository.save(song1);
        song2 = songRepository.save(song2);
        song3 = songRepository.save(song3);

        user = userRepository.save(user);

        recentlyPlayed1.setUser(user);
        recentlyPlayed2.setUser(user);
        recentlyPlayed3.setUser(user);
        recentlyPlayed1.setSong(song1);
        recentlyPlayed2.setSong(song2);
        recentlyPlayed3.setSong(song3);

        recentlyPlayed1 = recentlyPlayedRepository.save(recentlyPlayed1);
        recentlyPlayed2 = recentlyPlayedRepository.save(recentlyPlayed2);
        recentlyPlayed3 = recentlyPlayedRepository.save(recentlyPlayed3);
    }

    @Test
    public void shouldFindAllRecentlyPlayedSongsByUserIdOrderedByCreatedAtDesc() {
        final int pageSize = 3;
        final Pageable pageableForFirstPage = PageRequest.of(0, pageSize);
        final Pageable pageableForSecondPage = PageRequest.of(1, pageSize);

        final Page<RecentlyPlayed> pageWithSongs = recentlyPlayedRepository.findAllByUserIdOrderByCreatedAtDesc(
                pageableForFirstPage, user.getId()
        );
        final Page<RecentlyPlayed> emptyPage = recentlyPlayedRepository.findAllByUserIdOrderByCreatedAtDesc(
                pageableForSecondPage, user.getId()
        );

        assertEquals(pageSize, pageWithSongs.toSet().size(), WRONG_PAGE_SIZE);
        assertTrue(
                pageWithSongs.toSet().containsAll(List.of(recentlyPlayed1, recentlyPlayed2, recentlyPlayed3)),
                PAGE_DOES_NOT_CONTAIN_SPECIFIED_RECENTLY_PLAYED_SONGS
        );
        assertTrue(emptyPage.isEmpty(), PAGE_IS_NOT_EMPTY);
    }

    @Test
    public void shouldNotFindAnyRecentlyPlayedSongWhenUserDoesNotExist() {
        final Long unknownUserId = 5L;
        final Pageable pageable = PageRequest.of(0, 3);
        final Page<RecentlyPlayed> page = recentlyPlayedRepository.findAllByUserIdOrderByCreatedAtDesc(
                pageable, unknownUserId
        );

        assertTrue(page.isEmpty(), PAGE_IS_NOT_EMPTY);
    }
}
