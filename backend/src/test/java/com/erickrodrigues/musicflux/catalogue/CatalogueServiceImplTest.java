package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumService;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistService;
import com.erickrodrigues.musicflux.playlist.Playlist;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.playlist.PlaylistService;
import com.erickrodrigues.musicflux.track.TrackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogueServiceImplTest {

    @Mock
    private ArtistService artistService;

    @Mock
    private AlbumService albumService;

    @Mock
    private TrackService trackService;

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private CatalogueServiceImpl catalogueService;

    @Test
    public void findAllByTypesAndText() {
        final String text = "something not important";
        final List<SearchableType> types = List.of(
                SearchableType.ARTIST,
                SearchableType.ALBUM,
                SearchableType.TRACK,
                SearchableType.PLAYLIST
        );
        final List<Artist> artists = List.of(
                Artist.builder().id(1L).build(),
                Artist.builder().id(2L).build()
        );
        final List<Album> albums = List.of(
                Album.builder().id(1L).build(),
                Album.builder().id(2L).build()
        );
        final List<Track> tracks = List.of(
                Track.builder().id(1L).build(),
                Track.builder().id(2L).build()
        );
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).build(),
                Playlist.builder().id(2L).build()
        );

        when(artistService.findAllByNameContainingIgnoreCase(anyString())).thenReturn(artists);
        when(albumService.findAllByTitleContainingIgnoreCase(anyString())).thenReturn(albums);
        when(trackService.findAllByTitleContainingIgnoreCase(anyString())).thenReturn(tracks);
        when(playlistService.findAllByNameContainingIgnoreCase(anyString())).thenReturn(playlists);

        final CatalogueResult catalogueResult = catalogueService.findAllByTypesAndText(types, text);

        assertEquals(2, catalogueResult.getArtists().size());
        assertEquals(2, catalogueResult.getAlbums().size());
        assertEquals(2, catalogueResult.getTracks().size());
        assertEquals(2, catalogueResult.getPlaylists().size());
        verify(artistService, times(1)).findAllByNameContainingIgnoreCase(anyString());
        verify(albumService, times(1)).findAllByTitleContainingIgnoreCase(anyString());
        verify(trackService, times(1)).findAllByTitleContainingIgnoreCase(anyString());
        verify(playlistService, times(1)).findAllByNameContainingIgnoreCase(anyString());
    }

    @Test
    public void findAllByGenreName() {
        final String genre = "something not important";
        final Artist artist = Artist.builder().id(1L).build();
        final Album album = Album.builder().id(1L).artists(List.of(artist)).build();
        final List<Track> tracks = List.of(
                Track.builder().id(1L).album(album).build(),
                Track.builder().id(2L).album(album).build()
        );

        when(trackService.findAllByGenreName(genre)).thenReturn(tracks);

        final CatalogueResult catalogueResult = catalogueService.findAllByGenreName(genre);

        assertEquals(1, catalogueResult.getArtists().size());
        assertEquals(1, catalogueResult.getAlbums().size());
        assertEquals(2, catalogueResult.getTracks().size());
        assertEquals(0, catalogueResult.getPlaylists().size());
        verify(trackService, times(1)).findAllByGenreName(anyString());
    }
}
