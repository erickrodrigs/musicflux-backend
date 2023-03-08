package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.artist.ArtistMapper;
import com.erickrodrigues.musicflux.genre.Genre;
import com.erickrodrigues.musicflux.playlist.PlaylistDetailsDto;
import com.erickrodrigues.musicflux.playlist.PlaylistMapper;
import com.erickrodrigues.musicflux.song.SongDetailsDto;
import com.erickrodrigues.musicflux.song.SongMapper;
import com.erickrodrigues.musicflux.playlist.Playlist;
import com.erickrodrigues.musicflux.song.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @Mock
    private ArtistMapper artistMapper;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private SongMapper songMapper;

    @Mock
    private PlaylistMapper playlistMapper;

    @InjectMocks
    private SearchController searchController;

    @Test
    public void search() throws Exception {
        final List<SearchableType> types = List.of(
                SearchableType.ARTIST, SearchableType.ALBUM, SearchableType.SONG, SearchableType.PLAYLIST
        );
        final String text = "dark";
        final List<Artist> artists = List.of(
                Artist.builder().id(1L).name("Dark Angel").build()
        );
        final List<Album> albums = List.of(
                Album.builder().id(2L).title("The Dark Side Of The Moon").releaseDate(LocalDate.of(1973, 3, 1)).build()
        );
        final List<Song> songs = List.of(
                Song.builder().id(1L).title("Dark Fantasy").genres(List.of(Genre.builder().name("Hip-hop").build())).build()
        );
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("The Most Dark and Depressive Songs").build()
        );

        final List<ArtistDetailsDto> artistsDetailsDto = List.of(
                ArtistDetailsDto.builder().id(1L).name("Dark Angel").build()
        );
        final List<AlbumDetailsDto> albumsDetailsDto = List.of(
                AlbumDetailsDto.builder().id(2L).title("The Dark Side Of The Moon").releaseDate(LocalDate.of(1973, 3, 1)).artistsIds(List.of(1L)).build()
        );
        final List<SongDetailsDto> songsDetailsDto = List.of(
                SongDetailsDto.builder().id(1L).title("Dark Fantasy").genres(List.of("Hip-hop")).albumId(1L).build()
        );
        final List<PlaylistDetailsDto> playlistsDetailsDto = List.of(
                PlaylistDetailsDto.builder().id(1L).name("The Most Dark and Depressive Songs").userId(1L).build()
        );

        when(searchService.execute(types, text)).thenReturn(
                SearchResult.builder()
                        .artists(artists)
                        .albums(albums)
                        .songs(songs)
                        .playlists(playlists)
                        .build()
        );
        when(artistMapper.toArtistDetailsDto(artists.get(0))).thenReturn(artistsDetailsDto.get(0));
        when(albumMapper.toAlbumDetailsDto(albums.get(0))).thenReturn(albumsDetailsDto.get(0));
        when(songMapper.toSongDetailsDto(songs.get(0))).thenReturn(songsDetailsDto.get(0));
        when(playlistMapper.toPlaylistDetailsDto(playlists.get(0))).thenReturn(playlistsDetailsDto.get(0));

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("types", types.stream().map(String::valueOf).toList());
        params.add("value", text);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/catalogue")
                        .params(params))
                .andExpect(status().isOk())
                .andReturn();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final SearchResultDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                SearchResultDto.class
        );

        assertEquals(artistsDetailsDto.size(), actualResponse.getArtists().size());
        assertTrue(actualResponse.getArtists().containsAll(artistsDetailsDto));
        assertEquals(albumsDetailsDto.size(), actualResponse.getAlbums().size());
        assertTrue(actualResponse.getAlbums().containsAll(albumsDetailsDto));
        assertEquals(songsDetailsDto.size(), actualResponse.getSongs().size());
        assertTrue(actualResponse.getSongs().containsAll(songsDetailsDto));
        assertEquals(playlistsDetailsDto.size(), actualResponse.getPlaylists().size());
        assertTrue(actualResponse.getPlaylists().containsAll(playlistsDetailsDto));
        verify(searchService, times(1)).execute(anyList(), anyString());
        verify(artistMapper, times(1)).toArtistDetailsDto(any());
        verify(albumMapper, times(1)).toAlbumDetailsDto(any());
        verify(songMapper, times(1)).toSongDetailsDto(any());
        verify(playlistMapper, times(1)).toPlaylistDetailsDto(any());
    }
}
