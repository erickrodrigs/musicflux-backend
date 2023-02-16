package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private final Artist artist1 = Artist.builder().id(1L).build();
    private final Artist artist2 = Artist.builder().id(2L).build();
    private final Album album1 = Album.builder().id(1L).build();
    private final Album album2 = Album.builder().id(2L).build();
    private final Album album3 = Album.builder().id(3L).build();

    @BeforeEach
    void setUp() {
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

    @AfterEach
    void tearDown() {
        artistRepository.deleteAll();
        albumRepository.deleteAll();
    }

    @Test
    void findAllByArtistsIn() {
        Set<Album> albums = albumRepository.findAllByArtistsIn(Set.of(artist2.getId()));

        assertEquals(2, albums.size());
        assertTrue(albums.containsAll(Set.of(album1, album3)));
    }
}
