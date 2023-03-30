package com.erickrodrigues.musicflux.integration;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumRepository;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistRepository;
import com.erickrodrigues.musicflux.genre.Genre;
import com.erickrodrigues.musicflux.genre.GenreRepository;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackRepository;
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
public class TrackRepositoryTest {

    private static final String DIFFERENT_NUMBER_OF_TRACKS = "Different number of tracks found";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS = "Actual list does not contain specified tracks";

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    private static Artist artist = Artist
            .builder()
            .name("My favorite artist")
            .build();
    private static Album album = Album
            .builder()
            .title("My favorite album")
            .build();
    private static Genre genre1 = Genre
            .builder()
            .name("New Wave")
            .build();
    private static Genre genre2 = Genre
            .builder()
            .name("Hip-hop").
            build();
    private static Track track1 = Track
            .builder()
            .title("Burning Down The House")
            .build();
    private static Track track2 = Track
            .builder()
            .title("all i know is i wanna love you")
            .build();
    private static Track track3 = Track
            .builder()
            .title("I Wanna Love You")
            .build();
    private static Track track4 = Track
            .builder()
            .title("Let's Get Down")
            .build();

    @BeforeAll
    public void setUp() {
        artist = artistRepository.save(artist);
        album.setArtists(List.of(artist));
        album = albumRepository.save(album);
        artist.setAlbums(List.of(album));
        artist = artistRepository.save(artist);

        track1.setAlbum(album);
        track2.setAlbum(album);
        track3.setAlbum(album);
        track4.setAlbum(album);

        genre1 = genreRepository.save(genre1);
        genre2 = genreRepository.save(genre2);

        track1.setGenres(List.of(genre1));
        track2.setGenres(List.of(genre2));
        track3.setGenres(List.of(genre2));
        track4.setGenres(List.of(genre1));

        track1 = trackRepository.save(track1);
        track2 = trackRepository.save(track2);
        track3 = trackRepository.save(track3);
        track4 = trackRepository.save(track4);
    }

    @Test
    public void shouldFindAllTracksByTheirTitleContainingTextAndIgnoringCase() {
        final String text = "I WANNA LOVE YOU";
        final List<Track> tracks = trackRepository.findAllByTitleContainingIgnoreCase(text);

        assertEquals(2, tracks.size(), DIFFERENT_NUMBER_OF_TRACKS);
        assertTrue(tracks.containsAll(List.of(track2, track3)), LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS);
    }

    @Test
    public void shouldFindAllTracksByAGenreIgnoringCase() {
        final String genre = "new wave";
        final List<Track> tracks = trackRepository.findAllByGenresNameIgnoreCase(genre);

        assertEquals(2, tracks.size(), DIFFERENT_NUMBER_OF_TRACKS);
        assertTrue(tracks.containsAll(List.of(track1, track4)), LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS);
    }
}
