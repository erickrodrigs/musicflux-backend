package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.song.Song;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.song.SongRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FavoriteRepositoryTest {

    private static final String WRONG_NUMBER_OF_FAVORITES = "Wrong number of favorites";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_FAVORITES = "Actual list does not contain specified favorites";
    private static final String FAVORITE_WAS_NOT_FOUND = "Favorite of given song was not found";
    private static final String WRONG_FAVORITE_SONG_ID = "Wrong favorite song id";
    private static final String FAVORITE_SONG_WAS_FOUND_WITH_GIVEN_ID = "Favorite song was found with given id";

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    private static User user;
    private static Song song1;
    private static Song song2;
    private static Song song3;
    private static Favorite favorite1;
    private static Favorite favorite2;
    private static Favorite favorite3;

    @BeforeAll
    public void setUp() {
        song1 = Song.builder().id(1L).build();
        song2 = Song.builder().id(2L).build();
        song3 = Song.builder().id(3L).build();
        song1 = songRepository.save(song1);
        song2 = songRepository.save(song2);
        song3 = songRepository.save(song3);

        user = User.builder().id(1L).build();
        user = userRepository.save(user);

        favorite1 = Favorite.builder().song(song1).user(user).build();
        favorite2 = Favorite.builder().song(song2).user(user).build();
        favorite3 = Favorite.builder().song(song3).user(user).build();
        favorite1 = favoriteRepository.save(favorite1);
        favorite2 = favoriteRepository.save(favorite2);
        favorite3 = favoriteRepository.save(favorite3);
    }

    @Test
    public void shouldFindAllLikedSongsByUser() {
        final List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        assertEquals(3, favorites.size(), WRONG_NUMBER_OF_FAVORITES);
        assertTrue(favorites.containsAll(List.of(favorite1, favorite2, favorite3)), LIST_DOES_NOT_CONTAIN_SPECIFIED_FAVORITES);
    }

    @Test
    public void shouldNotFindAnyLikedSongByUserWhenTheyDoNotExist() {
        final Long unknownUserId = user.getId() + 498L;

        final List<Favorite> favorites = favoriteRepository.findAllByUserId(unknownUserId);

        assertEquals(0, favorites.size(), WRONG_NUMBER_OF_FAVORITES);
    }

    @Test
    public void shouldFindLikedSongByItsId() {
        final Long songId = song1.getId();

        final Optional<Favorite> optionalFavorite = favoriteRepository.findBySongId(songId);

        assertTrue(optionalFavorite.isPresent(), FAVORITE_WAS_NOT_FOUND);
        assertEquals(songId, optionalFavorite.get().getSong().getId(), WRONG_FAVORITE_SONG_ID);
    }

    @Test
    public void shouldNotFindLikedSongWhenItWasNotLiked() {
        final Long unknownSongId = song1.getId() + song2.getId() + song3.getId();

        final Optional<Favorite> optionalFavorite = favoriteRepository.findBySongId(unknownSongId);

        assertTrue(optionalFavorite.isEmpty(), FAVORITE_SONG_WAS_FOUND_WITH_GIVEN_ID);
    }
}
