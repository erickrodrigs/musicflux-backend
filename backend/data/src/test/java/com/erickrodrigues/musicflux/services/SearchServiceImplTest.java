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

import java.util.Set;

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
        final Set<Artist> artists = Set.of(
                Artist.builder().id(1L).build(),
                Artist.builder().id(2L).build()
        );
        final Set<Album> albums = Set.of(
                Album.builder().id(1L).build(),
                Album.builder().id(2L).build()
        );
        final Set<Song> songs = Set.of(
                Song.builder().id(1L).build(),
                Song.builder().id(2L).build()
        );
        final Set<Playlist> playlists = Set.of(
                Playlist.builder().id(1L).build(),
                Playlist.builder().id(2L).build()
        );

        when(artistService.findAllByName(anyString())).thenReturn(artists);
        when(albumService.findAllByTitle(anyString())).thenReturn(albums);
        when(songService.findAllByTitle(anyString())).thenReturn(songs);
        when(playlistService.findAllByName(anyString())).thenReturn(playlists);
    }

    @Test
    public void execute() {
        final String text = "something not important";
        final Set<SearchableType> types = Set.of(
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
        verify(artistService, times(1)).findAllByName(anyString());
        verify(albumService, times(1)).findAllByTitle(anyString());
        verify(songService, times(1)).findAllByTitle(anyString());
        verify(playlistService, times(1)).findAllByName(anyString());
    }
}
