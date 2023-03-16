package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.genre.Genre;
import com.erickrodrigues.musicflux.genre.GenreRepository;
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
public class SongRepositoryTest {

    private static final String DIFFERENT_NUMBER_OF_SONGS = "Different number of songs found";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_SONGS = "Actual list does not contain specified songs";

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private GenreRepository genreRepository;

    private static Genre genre1 = Genre
            .builder()
            .name("New Wave")
            .build();
    private static Genre genre2 = Genre
            .builder()
            .name("Hip-hop").
            build();
    private static Song song1 = Song
            .builder()
            .title("Burning Down The House")
            .build();
    private static Song song2 = Song
            .builder()
            .title("all i know is i wanna love you")
            .build();
    private static Song song3 = Song
            .builder()
            .title("I Wanna Love You")
            .build();
    private static Song song4 = Song
            .builder()
            .title("Let's Get Down")
            .build();

    @BeforeAll
    public void setUp() {
        genre1 = genreRepository.save(genre1);
        genre2 = genreRepository.save(genre2);

        song1.setGenres(List.of(genre1));
        song2.setGenres(List.of(genre2));
        song3.setGenres(List.of(genre2));
        song4.setGenres(List.of(genre1));

        song1 = songRepository.save(song1);
        song2 = songRepository.save(song2);
        song3 = songRepository.save(song3);
        song4 = songRepository.save(song4);
    }

    @Test
    public void shouldFindAllSongsByTheirTitleContainingTextAndIgnoringCase() {
        final String text = "I WANNA LOVE YOU";
        final List<Song> songs = songRepository.findAllByTitleContainingIgnoreCase(text);

        assertEquals(2, songs.size(), DIFFERENT_NUMBER_OF_SONGS);
        assertTrue(songs.containsAll(List.of(song2, song3)), LIST_DOES_NOT_CONTAIN_SPECIFIED_SONGS);
    }

    @Test
    public void shouldFindAllSongsByAGenreIgnoringCase() {
        final String genre = "new wave";
        final List<Song> songs = songRepository.findAllByGenresNameIgnoreCase(genre);

        assertEquals(2, songs.size(), DIFFERENT_NUMBER_OF_SONGS);
        assertTrue(songs.containsAll(List.of(song1, song4)), LIST_DOES_NOT_CONTAIN_SPECIFIED_SONGS);
    }
}
