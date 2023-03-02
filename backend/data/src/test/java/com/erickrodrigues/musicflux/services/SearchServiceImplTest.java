package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;
import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.vo.SearchResult;
import com.erickrodrigues.musicflux.vo.SearchableType;
import org.junit.jupiter.api.BeforeEach;
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
public class SearchServiceImplTest {

    @Mock
    private ArtistService artistService;

    @Mock
    private AlbumService albumService;

    @Mock
    private SongService songService;

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private SearchServiceImpl searchService;

    @BeforeEach
    public void setUp() {
        final List<Artist> artists = List.of(
                Artist.builder().id(1L).build(),
                Artist.builder().id(2L).build()
        );
        final List<Album> albums = List.of(
                Album.builder().id(1L).build(),
                Album.builder().id(2L).build()
        );
        final List<Song> songs = List.of(
                Song.builder().id(1L).build(),
                Song.builder().id(2L).build()
        );
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).build(),
                Playlist.builder().id(2L).build()
        );

        when(artistService.findAllByNameContainingIgnoreCase(anyString())).thenReturn(artists);
        when(albumService.findAllByTitleContainingIgnoreCase(anyString())).thenReturn(albums);
        when(songService.findAllByTitleContainingIgnoreCase(anyString())).thenReturn(songs);
        when(playlistService.findAllByNameContainingIgnoreCase(anyString())).thenReturn(playlists);
    }

    @Test
    public void execute() {
        final String text = "something not important";
        final List<SearchableType> types = List.of(
                SearchableType.ARTIST,
                SearchableType.ALBUM,
                SearchableType.SONG,
                SearchableType.PLAYLIST
        );

        final SearchResult searchResult = searchService.execute(types, text);

        assertEquals(2, searchResult.getArtists().size());
        assertEquals(2, searchResult.getAlbums().size());
        assertEquals(2, searchResult.getSongs().size());
        assertEquals(2, searchResult.getPlaylists().size());
        verify(artistService, times(1)).findAllByNameContainingIgnoreCase(anyString());
        verify(albumService, times(1)).findAllByTitleContainingIgnoreCase(anyString());
        verify(songService, times(1)).findAllByTitleContainingIgnoreCase(anyString());
        verify(playlistService, times(1)).findAllByNameContainingIgnoreCase(anyString());
    }
}
