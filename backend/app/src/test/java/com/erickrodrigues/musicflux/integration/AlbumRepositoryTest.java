package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumRepository;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistRepository;
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
public class AlbumRepositoryTest {

    private static final String WRONG_NUMBER_OF_ALBUMS = "Wrong number of albums";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_ALBUMS = "Actual list does not contain specified albums";

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private static Artist artist;
    private static Album album1;
    private static Album album2;
    private static Album album3;

    @BeforeAll
    void setUp() {
        artist = Artist.builder().build();
        artist = artistRepository.save(artist);

        album1 = Album
                .builder()
                .title("my untitled album")
                .artists(List.of(artist))
                .build();
        album2 = Album
                .builder()
                .title("THIS ALBUM IS UNTITLED")
                .artists(List.of(artist))
                .build();
        album3 = Album
                .builder()
                .title("Master of Puppets")
                .artists(List.of(artist))
                .build();
        album1 = albumRepository.save(album1);
        album2 = albumRepository.save(album2);
        album3 = albumRepository.save(album3);

        artist.setAlbums(List.of(album1, album2, album3));
        artist = artistRepository.save(artist);
    }

    @Test
    public void shouldFindAllAlbumsByTitleContainingTextAndIgnoringCase() {
        final String text = "untitled";

        final List<Album> albums = albumRepository.findAllByTitleContainingIgnoreCase(text);

        assertEquals(2, albums.size(), WRONG_NUMBER_OF_ALBUMS);
        assertTrue(albums.containsAll(List.of(album1, album2)), LIST_DOES_NOT_CONTAIN_SPECIFIED_ALBUMS);
    }

    @Test
    void shouldFindAllAlbumsByArtists() {
        final List<Album> albums = albumRepository.findAllByArtistsId(artist.getId());

        assertEquals(3, albums.size(), WRONG_NUMBER_OF_ALBUMS);
        assertTrue(albums.containsAll(List.of(album1, album2, album3)), LIST_DOES_NOT_CONTAIN_SPECIFIED_ALBUMS);
    }
}
