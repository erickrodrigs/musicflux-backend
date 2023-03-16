package com.erickrodrigues.musicflux.artist;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArtistRepositoryTest {

    private static final String WRONG_NUMBER_OF_ARTISTS = "Wrong number of artists";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_ARTISTS = "Actual list does not contain specified artists";

    @Autowired
    private ArtistRepository artistRepository;

    private static Artist ironMaiden = Artist
            .builder()
            .name("Iron Maiden")
            .build();
    private static Artist ironSavior = Artist
            .builder()
            .name("Iron Savior")
            .build();
    private static Artist metallica = Artist
            .builder()
            .name("Metallica")
            .build();

    @BeforeAll
    public void setUp() {
        ironMaiden = artistRepository.save(ironMaiden);
        ironSavior = artistRepository.save(ironSavior);
        metallica = artistRepository.save(metallica);
    }

    @Test
    public void shouldFindAllArtistsByNameContainingTextAndIgnoringCase() {
        final String ironText = "iron", metaText = "meta";

        final List<Artist> artistsWithIron = artistRepository.findAllByNameContainingIgnoreCase(ironText);
        final List<Artist> artistsWithMeta = artistRepository.findAllByNameContainingIgnoreCase(metaText);

        assertEquals(2, artistsWithIron.size(), WRONG_NUMBER_OF_ARTISTS);
        assertEquals(1, artistsWithMeta.size(), WRONG_NUMBER_OF_ARTISTS);
        assertTrue(artistsWithIron.containsAll(List.of(ironMaiden, ironSavior)), LIST_DOES_NOT_CONTAIN_SPECIFIED_ARTISTS);
        assertTrue(artistsWithMeta.contains(metallica), LIST_DOES_NOT_CONTAIN_SPECIFIED_ARTISTS);
    }
}
