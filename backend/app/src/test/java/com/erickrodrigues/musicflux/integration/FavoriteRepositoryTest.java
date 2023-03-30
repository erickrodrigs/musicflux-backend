package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.favorite.Favorite;
import com.erickrodrigues.musicflux.favorite.FavoriteRepository;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.user.User;
import com.erickrodrigues.musicflux.user.UserRepository;
import com.erickrodrigues.musicflux.track.TrackRepository;
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
    private static final String FAVORITE_WAS_NOT_FOUND = "Favorite of given track was not found";
    private static final String WRONG_FAVORITE_TRACK_ID = "Wrong favorite track id";
    private static final String FAVORITE_TRACK_WAS_FOUND_WITH_GIVEN_ID = "Favorite track was found with given id";

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrackRepository trackRepository;

    private static User user;
    private static Track track1;
    private static Track track2;
    private static Track track3;
    private static Favorite favorite1;
    private static Favorite favorite2;
    private static Favorite favorite3;

    @BeforeAll
    public void setUp() {
        track1 = Track.builder().id(1L).build();
        track2 = Track.builder().id(2L).build();
        track3 = Track.builder().id(3L).build();
        track1 = trackRepository.save(track1);
        track2 = trackRepository.save(track2);
        track3 = trackRepository.save(track3);

        user = User.builder().id(1L).build();
        user = userRepository.save(user);

        favorite1 = Favorite.builder().track(track1).user(user).build();
        favorite2 = Favorite.builder().track(track2).user(user).build();
        favorite3 = Favorite.builder().track(track3).user(user).build();
        favorite1 = favoriteRepository.save(favorite1);
        favorite2 = favoriteRepository.save(favorite2);
        favorite3 = favoriteRepository.save(favorite3);
    }

    @Test
    public void shouldFindAllLikedTracksByUser() {
        final List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        assertEquals(3, favorites.size(), WRONG_NUMBER_OF_FAVORITES);
        assertTrue(favorites.containsAll(List.of(favorite1, favorite2, favorite3)), LIST_DOES_NOT_CONTAIN_SPECIFIED_FAVORITES);
    }

    @Test
    public void shouldNotFindAnyLikedTrackByUserWhenTheyDoNotExist() {
        final Long unknownUserId = user.getId() + 498L;

        final List<Favorite> favorites = favoriteRepository.findAllByUserId(unknownUserId);

        assertEquals(0, favorites.size(), WRONG_NUMBER_OF_FAVORITES);
    }

    @Test
    public void shouldFindLikedTrackByItsId() {
        final Long trackId = track1.getId();

        final Optional<Favorite> optionalFavorite = favoriteRepository.findByTrackId(trackId);

        assertTrue(optionalFavorite.isPresent(), FAVORITE_WAS_NOT_FOUND);
        assertEquals(trackId, optionalFavorite.get().getTrack().getId(), WRONG_FAVORITE_TRACK_ID);
    }

    @Test
    public void shouldNotFindLikedTrackWhenItWasNotLiked() {
        final Long unknownTrackId = track1.getId() + track2.getId() + track3.getId();

        final Optional<Favorite> optionalFavorite = favoriteRepository.findByTrackId(unknownTrackId);

        assertTrue(optionalFavorite.isEmpty(), FAVORITE_TRACK_WAS_FOUND_WITH_GIVEN_ID);
    }
}
