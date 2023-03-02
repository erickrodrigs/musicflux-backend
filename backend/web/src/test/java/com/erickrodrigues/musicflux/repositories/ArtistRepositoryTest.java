package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Artist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    private Artist artist1, artist2, artist3;

    @BeforeEach
    public void setUp() {
        artist1 = Artist.builder().id(1L).name("Iron Maiden").build();
        artist2 = Artist.builder().id(2L).name("Iron Savior").build();
        artist3 = Artist.builder().id(3L).name("Metallica").build();

        artistRepository.save(artist1);
        artistRepository.save(artist2);
        artistRepository.save(artist3);
    }

    @Test
    public void findAllByNameContainingIgnoreCase() {
        List<Artist> artists;

        artists = artistRepository.findAllByNameContainingIgnoreCase("iron");

        assertEquals(2, artists.size());
        assertTrue(artists.containsAll(List.of(artist1, artist2)));

        artists = artistRepository.findAllByNameContainingIgnoreCase("meta");

        assertEquals(1, artists.size());
        assertTrue(artists.contains(artist3));
    }
}
