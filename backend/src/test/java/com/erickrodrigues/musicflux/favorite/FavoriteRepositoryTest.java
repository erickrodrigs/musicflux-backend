package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    private Favorite favorite1, favorite2, favorite3;

    @BeforeEach
    public void setUp() {
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song1 = Song.builder().id(1L).build();
        final Song song2 = Song.builder().id(2L).build();
        final Song song3 = Song.builder().id(3L).build();
        favorite1 = Favorite.builder()
                .id(1L)
                .song(song1)
                .user(user)
                .build();
        favorite2 = Favorite.builder()
                .id(2L)
                .song(song2)
                .user(user)
                .build();
        favorite3 = Favorite.builder()
                .id(3L)
                .song(song3)
                .user(user)
                .build();

        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);
        userRepository.save(user);
        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);
        favoriteRepository.save(favorite3);
    }

    @Test
    public void findAllByUserId() {
        final List<Favorite> favorites = favoriteRepository.findAllByUserId(1L);

        assertEquals(3, favorites.size());
        assertTrue(favorites.containsAll(List.of(favorite1, favorite2, favorite3)));
    }

    @Test
    public void findAllByUserIdWhenItDoesNotExist() {
        final Long unknownUserId = 3L;
        final List<Favorite> favorites = favoriteRepository.findAllByUserId(unknownUserId);

        assertNotNull(favorites);
        assertEquals(0, favorites.size());
    }

    @Test
    public void findBySongId() {
        final Long songId = 1L;
        final Optional<Favorite> optionalFavorite = favoriteRepository.findBySongId(songId);

        assertTrue(optionalFavorite.isPresent());
        assertEquals(songId, optionalFavorite.get().getSong().getId());
    }

    @Test
    public void findBySongIdWhenItWasNotLiked() {
        final Long songId = 498L;
        final Optional<Favorite> optionalFavorite = favoriteRepository.findBySongId(songId);

        assertTrue(optionalFavorite.isEmpty());
    }
}
