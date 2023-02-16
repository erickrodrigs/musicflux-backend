package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private Album album1, album2;
    private Song song1, song2, song3, song4;

    @BeforeEach
    public void setUp() {
        song1 = Song.builder().id(1L).title("Burning Down The House").build();
        song2 = Song.builder().id(2L).title("all i know is i wanna love you").build();
        song3 = Song.builder().id(3L).title("I Wanna Love You").build();
        song4 = Song.builder().id(4L).title("Still D.R.E").build();
        album1 = Album.builder().id(1L).songs(Set.of(song2, song3)).build();
        album2 = Album.builder().id(2L).songs(Set.of(song1, song4)).build();
        song1.setAlbum(album2);
        song2.setAlbum(album1);
        song3.setAlbum(album1);
        song4.setAlbum(album2);

        albumRepository.save(album1);
        albumRepository.save(album2);
        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);
        songRepository.save(song4);
    }

    @Test
    public void findAllByTitleIsContainingIgnoreCase() {
        Set<Song> songs = songRepository.findAllByTitleContainingIgnoreCase("I WANNA LOVE YOU");

        assertEquals(2, songs.size());
        assertTrue(songs.containsAll(Set.of(song2, song3)));
    }

    @Test
    void findAllByAlbumId() {
        Set<Song> songs;

        songs = songRepository.findAllByAlbumId(album1.getId());

        assertEquals(2, songs.size());
        assertTrue(songs.containsAll(Set.of(song2, song3)));

        songs = songRepository.findAllByAlbumId(album2.getId());

        assertEquals(2, songs.size());
        assertTrue(songs.containsAll(Set.of(song1, song4)));
    }
}
