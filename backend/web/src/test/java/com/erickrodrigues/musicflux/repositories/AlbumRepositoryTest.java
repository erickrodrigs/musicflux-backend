package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
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
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private Artist artist1, artist2;
    private Album album1, album2, album3;

    @BeforeEach
    void setUp() {
        artist1 = Artist.builder().id(1L).build();
        artist2 = Artist.builder().id(2L).build();
        album1 = Album.builder().id(1L).build();
        album2 = Album.builder().id(2L).build();
        album3 = Album.builder().id(3L).build();

        artistRepository.save(artist1);
        artistRepository.save(artist2);
        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);

        artist1.setAlbums(Set.of(album1, album2));
        artist2.setAlbums(Set.of(album1, album3));
        album1.setArtists(Set.of(artist1, artist2));
        album2.setArtists(Set.of(artist1));
        album3.setArtists(Set.of(artist2));

        artistRepository.save(artist1);
        artistRepository.save(artist2);
        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);
    }

    @Test
    void findAllByArtistsIn() {
        Set<Album> albums;

        albums = albumRepository.findAllByArtistsIn(Set.of(artist1.getId()));

        assertEquals(2, albums.size());
        assertTrue(albums.containsAll(Set.of(album1, album2)));

        albums = albumRepository.findAllByArtistsIn(Set.of(artist2.getId()));

        assertEquals(2, albums.size());
        assertTrue(albums.containsAll(Set.of(album1, album3)));
    }
}
