package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

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

    @Test
    public void findAllByUserId() {
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        final Song song1 = Song.builder().id(1L).build();
        final Song song2 = Song.builder().id(2L).build();
        final Song song3 = Song.builder().id(3L).build();
        final Favorite favorite1 = Favorite.builder()
                .id(1L)
                .song(song1)
                .user(user)
                .build();
        final Favorite favorite2 = Favorite.builder()
                .id(2L)
                .song(song2)
                .user(user)
                .build();
        final Favorite favorite3 = Favorite.builder()
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

        final List<Favorite> favorites = favoriteRepository.findAllByUserId(userId);

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
}
