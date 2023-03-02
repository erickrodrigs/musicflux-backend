package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Profile;
import com.erickrodrigues.musicflux.domain.Favorite;
import com.erickrodrigues.musicflux.domain.Song;
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
    private ProfileRepository profileRepository;

    @Autowired
    private SongRepository songRepository;

    @Test
    public void findAllByProfileId() {
        final Long profileId = 1L;
        final Profile profile = Profile.builder().id(profileId).build();
        final Song song1 = Song.builder().id(1L).build();
        final Song song2 = Song.builder().id(2L).build();
        final Song song3 = Song.builder().id(3L).build();
        final Favorite favorite1 = Favorite.builder()
                .id(1L)
                .song(song1)
                .profile(profile)
                .build();
        final Favorite favorite2 = Favorite.builder()
                .id(2L)
                .song(song2)
                .profile(profile)
                .build();
        final Favorite favorite3 = Favorite.builder()
                .id(3L)
                .song(song3)
                .profile(profile)
                .build();

        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);
        profileRepository.save(profile);
        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);
        favoriteRepository.save(favorite3);

        final List<Favorite> favorites = favoriteRepository.findAllByProfileId(profileId);

        assertEquals(3, favorites.size());
        assertTrue(favorites.containsAll(List.of(favorite1, favorite2, favorite3)));
    }

    @Test
    public void findAllByProfileIdWhenItDoesNotExist() {
        final Long unknownProfileId = 3L;
        final List<Favorite> favorites = favoriteRepository.findAllByProfileId(unknownProfileId);

        assertNotNull(favorites);
        assertEquals(0, favorites.size());
    }
}
