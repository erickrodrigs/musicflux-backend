package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumService;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistService;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceImplTest {

    @Mock
    private ArtistService artistService;

    @Mock
    private AlbumService albumService;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Test
    public void shouldSearchForArtists() {
        // given
        final String text = "something not important";
        final SearchableType type = SearchableType.ARTIST;
        final List<Artist> artists = List.of(
                Artist.builder().id(1L).build(),
                Artist.builder().id(2L).build()
        );
        when(artistService.findAllByNameContainingIgnoreCase(text)).thenReturn(artists);

        // when
        final SearchResult searchResult = searchService.findAllByTypeAndText(type, text);

        // then
        assertEquals(artists.size(), searchResult.getArtists().size());
        verify(artistService, times(1)).findAllByNameContainingIgnoreCase(text);
    }

    @Test
    public void shouldSearchForAlbums() {
        // given
        final String text = "something not important";
        final SearchableType type = SearchableType.ALBUM;
        final List<Album> albums = List.of(
                Album.builder().id(1L).build(),
                Album.builder().id(2L).build()
        );
        when(albumService.findAllByTitleContainingIgnoreCase(text)).thenReturn(albums);

        // when
        final SearchResult searchResult = searchService.findAllByTypeAndText(type, text);

        // then
        assertEquals(albums.size(), searchResult.getAlbums().size());
        verify(albumService, times(1)).findAllByTitleContainingIgnoreCase(text);
    }

    @Test
    public void shouldSearchForTracks() {
        // given
        final String text = "something not important";
        final SearchableType type = SearchableType.TRACK;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).build(),
                Track.builder().id(2L).build()
        );
        when(trackService.findAllByTitleContainingIgnoreCase(text)).thenReturn(tracks);

        // when
        final SearchResult searchResult = searchService.findAllByTypeAndText(type, text);

        // then
        assertEquals(tracks.size(), searchResult.getTracks().size());
        verify(trackService, times(1)).findAllByTitleContainingIgnoreCase(text);
    }

    @Test
    public void shouldSearchByGenre() {
        // given
        final String text = "something not important";
        final SearchableType type = SearchableType.GENRE;
        final Artist artist = Artist.builder().id(1L).build();
        final Album album = Album.builder().id(1L).artists(List.of(artist)).build();
        final List<Track> tracks = List.of(
                Track.builder().id(1L).album(album).build(),
                Track.builder().id(2L).album(album).build()
        );
        when(trackService.findAllByGenreName(text)).thenReturn(tracks);

        // when
        final SearchResult searchResult = searchService.findAllByTypeAndText(type, text);

        assertEquals(1, searchResult.getArtists().size());
        assertEquals(1, searchResult.getAlbums().size());
        assertEquals(2, searchResult.getTracks().size());
        verify(trackService, times(1)).findAllByGenreName(text);
    }
}
