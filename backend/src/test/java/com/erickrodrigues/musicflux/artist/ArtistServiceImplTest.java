package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.track.Track;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceImplTest {

    private static final String WRONG_NUMBER_OF_ARTISTS = "Wrong number of artists";
    private static final String WRONG_NUMBER_OF_ALBUMS = "Wrong number of albums";
    private static final String WRONG_ORDER_FOR_MOST_PLAYED_TRACKS = "Wrong order for most played tracks";

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistServiceImpl artistService;

    @Test
    public void shouldFindAllArtistByTheirNameContainingTextAndIgnoringCase() {
        // given
        final String text = "iron";
        final List<Artist> artists = List.of(
                Artist.builder().id(1L).name("Iron Maiden").build(),
                Artist.builder().id(2L).name("Iron Savior").build()
        );
        when(artistRepository.findAllByNameContainingIgnoreCase(text)).thenReturn(artists);

        // when
        final List<Artist> actualArtists = artistService.findAllByNameContainingIgnoreCase(text);

        assertEquals(artists.size(), actualArtists.size(), WRONG_NUMBER_OF_ARTISTS);
        verify(artistRepository, times(1)).findAllByNameContainingIgnoreCase(text);
    }

    @Test
    public void shouldFindAllAlbumsByArtistId() {
        // given
        final Long artistId = 1L;
        final List<Album> albums = List.of(
                Album.builder().id(1L).title("Master of Puppets").build(),
                Album.builder().id(2L).title("Ride the Lightning").build()
        );
        final Artist artist = Artist.builder().id(artistId).albums(albums).build();
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        // when
        final List<Album> actualAlbums = artistService.getArtistAlbums(artistId);

        // then
        assertEquals(albums.size(), actualAlbums.size(), WRONG_NUMBER_OF_ALBUMS);
        verify(artistRepository, times(1)).findById(artistId);
    }

    @Test
    public void shouldThrowAnExceptionWhenFindingAlbumsByArtistIdThatDoesNotExist() {
        // given
        final Long artistId = 1L;
        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> artistService.getArtistAlbums(artistId));
        verify(artistRepository, times(1)).findById(artistId);
    }

    @Test
    public void shouldReturnTopTracksByArtistId() {
        // given
        final Long artistId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).numberOfPlays(5400L).build(),
                Track.builder().id(2L).numberOfPlays(400L).build(),
                Track.builder().id(3L).numberOfPlays(7600L).build(),
                Track.builder().id(4L).numberOfPlays(1000L).build(),
                Track.builder().id(5L).numberOfPlays(9000L).build(),
                Track.builder().id(6L).numberOfPlays(7200L).build()
        );
        final Album album = Album.builder().tracks(tracks).build();
        final Artist artist = Artist.builder().id(artistId).albums(List.of(album)).build();
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        // when
        final String topTracksIds = artistService.getTopTracks(artistId)
                .stream()
                .map(Track::getId)
                .toList()
                .toString();

        // then
        assertEquals("[5, 3, 6, 1, 4]", topTracksIds, WRONG_ORDER_FOR_MOST_PLAYED_TRACKS);
        verify(artistRepository, times(1)).findById(artistId);
    }

    @Test
    public void shouldReturnLessThanFiveTopTracksWhenArtistHasLessThanFiveTracks() {
        // given
        final Long artistId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).numberOfPlays(5400L).build(),
                Track.builder().id(2L).numberOfPlays(400L).build(),
                Track.builder().id(3L).numberOfPlays(7600L).build()
        );
        final Album album = Album.builder().tracks(tracks).build();
        final Artist artist = Artist.builder().id(artistId).albums(List.of(album)).build();
        when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        // when
        final String topTracksIds = artistService.getTopTracks(artistId)
                .stream()
                .map(Track::getId)
                .toList()
                .toString();

        // then
        assertEquals("[3, 1, 2]", topTracksIds, WRONG_ORDER_FOR_MOST_PLAYED_TRACKS);
        verify(artistRepository, times(1)).findById(artistId);
    }
}
