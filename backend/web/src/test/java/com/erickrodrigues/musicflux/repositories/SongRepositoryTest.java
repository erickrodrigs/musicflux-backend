package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Song;
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

    private Song song1, song2, song3, song4;

    @BeforeEach
    public void setUp() {
        song1 = Song.builder().id(1L).title("Burning Down The House").build();
        song2 = Song.builder().id(2L).title("all i know is i wanna love you").build();
        song3 = Song.builder().id(3L).title("I Wanna Love You").build();
        song4 = Song.builder().id(4L).title("Let's Get Down").build();

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
}
