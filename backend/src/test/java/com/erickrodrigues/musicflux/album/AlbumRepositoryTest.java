package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistRepository;
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
        album1 = Album.builder().id(1L).title("my untitled album").build();
        album2 = Album.builder().id(2L).title("THIS ALBUM IS UNTITLED").build();
        album3 = Album.builder().id(3L).title("Master of Puppets").build();

        artistRepository.save(artist1);
        artistRepository.save(artist2);
        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);

        artist1.setAlbums(List.of(album1, album2));
        artist2.setAlbums(List.of(album1, album3));
        album1.setArtists(List.of(artist1, artist2));
        album2.setArtists(List.of(artist1));
        album3.setArtists(List.of(artist2));

        artistRepository.save(artist1);
        artistRepository.save(artist2);
        albumRepository.save(album1);
        albumRepository.save(album2);
        albumRepository.save(album3);
    }

    @Test
    public void findAllByTitleContainingIgnoreCase() {
        List<Album> albums = albumRepository.findAllByTitleContainingIgnoreCase("untitled");

        assertEquals(2, albums.size());
        assertTrue(albums.containsAll(List.of(album1, album2)));
    }

    @Test
    void findAllByArtistsId() {
        List<Album> albums;

        albums = albumRepository.findAllByArtistsId(artist1.getId());

        assertEquals(2, albums.size());
        assertTrue(albums.containsAll(List.of(album1, album2)));

        albums = albumRepository.findAllByArtistsId(artist2.getId());

        assertEquals(2, albums.size());
        assertTrue(albums.containsAll(List.of(album1, album3)));
    }
}
