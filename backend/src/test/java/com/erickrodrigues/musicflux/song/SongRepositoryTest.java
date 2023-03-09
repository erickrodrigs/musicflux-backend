package com.erickrodrigues.musicflux.song;

import com.erickrodrigues.musicflux.genre.Genre;
import com.erickrodrigues.musicflux.genre.GenreRepository;
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
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Song song1, song2, song3, song4;

    @BeforeEach
    public void setUp() {
        final Genre genre1 = Genre.builder().id(1L).name("New Wave").build();
        final Genre genre2 = Genre.builder().id(2L).name("Hip-hop").build();

        song1 = Song.builder()
                .id(1L)
                .title("Burning Down The House")
                .genres(List.of(genre1))
                .build();
        song2 = Song.builder()
                .id(2L)
                .title("all i know is i wanna love you")
                .genres(List.of(genre2))
                .build();
        song3 = Song.builder()
                .id(3L)
                .title("I Wanna Love You")
                .genres(List.of(genre2))
                .build();
        song4 = Song.builder()
                .id(4L)
                .title("Let's Get Down")
                .genres(List.of(genre1))
                .build();

        genreRepository.save(genre1);
        genreRepository.save(genre2);

        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);
        songRepository.save(song4);
    }

    @Test
    public void findAllByTitleIsContainingIgnoreCase() {
        List<Song> songs;

        songs = songRepository.findAllByTitleContainingIgnoreCase("I WANNA LOVE YOU");

        assertEquals(2, songs.size());
        assertTrue(songs.containsAll(List.of(song2, song3)));

        songs = songRepository.findAllByTitleContainingIgnoreCase("DOWN");

        assertEquals(2, songs.size());
        assertTrue(songs.containsAll(List.of(song1, song4)));
    }

    @Test
    public void findAllByGenresNameIgnoreCase() {
        final List<Song> songs = songRepository.findAllByGenresNameIgnoreCase("new wave");

        assertEquals(2, songs.size());
        assertTrue(songs.containsAll(List.of(song1, song4)));
    }
}
