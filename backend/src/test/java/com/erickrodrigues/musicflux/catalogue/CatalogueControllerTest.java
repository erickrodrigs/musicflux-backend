package com.erickrodrigues.musicflux.catalogue;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
import com.erickrodrigues.musicflux.genre.Genre;
import com.erickrodrigues.musicflux.playlist.PlaylistDto;
import com.erickrodrigues.musicflux.track.TrackDto;
import com.erickrodrigues.musicflux.playlist.Playlist;
import com.erickrodrigues.musicflux.track.Track;
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
public class CatalogueControllerTest {

    @Mock
    private CatalogueService catalogueService;

    @Mock
    private CatalogueMapper catalogueMapper;

    @InjectMocks
    private CatalogueController catalogueController;

    @Test
    public void search() throws Exception {
        final List<SearchableType> types = List.of(
                SearchableType.ARTIST, SearchableType.ALBUM, SearchableType.TRACK, SearchableType.PLAYLIST
        );
        final String text = "dark";
        final CatalogueResult catalogueResult = CatalogueResult
                .builder()
                .artists(List.of(
                        Artist.builder().id(1L).name("Dark Angel").build()
                ))
                .albums(List.of(
                        Album.builder().id(2L).title("The Dark Side Of The Moon").releaseDate(LocalDate.of(1973, 3, 1)).build()
                ))
                .tracks(List.of(
                        Track.builder().id(1L).title("Dark Fantasy").genres(List.of(Genre.builder().name("Hip-hop").build())).build()
                ))
                .playlists(List.of(
                        Playlist.builder().id(1L).name("The Most Dark and Depressive Tracks").build()
                ))
                .build();
        final CatalogueResultDto catalogueResultDto = CatalogueResultDto
                .builder()
                .artists(List.of(
                        ArtistDetailsDto.builder().id(1L).name("Dark Angel").build()
                ))
                .albums(List.of(
                        AlbumDetailsDto.builder().id(2L).title("The Dark Side Of The Moon").releaseDate(LocalDate.of(1973, 3, 1)).artistsIds(List.of(1L)).build()
                ))
                .tracks(List.of(
                        TrackDto.builder().id(1L).title("Dark Fantasy").genres(List.of("Hip-hop")).albumId(1L).build()
                ))
                .playlists(List.of(
                        PlaylistDto.builder().id(1L).name("The Most Dark and Depressive Tracks").userId(1L).build()
                ))
                .build();

        when(catalogueService.findAllByTypesAndText(types, text)).thenReturn(catalogueResult);
        when(catalogueMapper.toCatalogueResultDto(catalogueResult)).thenReturn(catalogueResultDto);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("types", types.stream().map(String::valueOf).toList());
        params.add("value", text);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(catalogueController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/catalogue")
                        .params(params))
                .andExpect(status().isOk())
                .andReturn();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final CatalogueResultDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CatalogueResultDto.class
        );

        assertEquals(catalogueResultDto.getArtists().size(), actualResponse.getArtists().size());
        assertEquals(catalogueResultDto.getAlbums().size(), actualResponse.getAlbums().size());
        assertEquals(catalogueResultDto.getTracks().size(), actualResponse.getTracks().size());
        assertEquals(catalogueResultDto.getPlaylists().size(), actualResponse.getPlaylists().size());
        assertTrue(actualResponse.getArtists().containsAll(catalogueResultDto.getArtists()));
        assertTrue(actualResponse.getAlbums().containsAll(catalogueResultDto.getAlbums()));
        assertTrue(actualResponse.getTracks().containsAll(catalogueResultDto.getTracks()));
        assertTrue(actualResponse.getPlaylists().containsAll(catalogueResultDto.getPlaylists()));
        verify(catalogueService, times(1)).findAllByTypesAndText(anyList(), anyString());
        verify(catalogueMapper, times(1)).toCatalogueResultDto(any());
    }

    @Test
    public void findAllByGenre() throws Exception {
        final String genre = "synth-pop";
        final Artist artist = Artist.builder().id(1L).build();
        final Album album = Album.builder().id(1L).artists(List.of(artist)).build();
        final List<Track> tracks = List.of(
                Track.builder().id(1L).album(album).build(),
                Track.builder().id(2L).album(album).build()
        );
        final ArtistDetailsDto artistDetailsDto = ArtistDetailsDto.builder().id(1L).build();
        final AlbumDetailsDto albumDetailsDto = AlbumDetailsDto.builder().id(1L).artistsIds(List.of(artist.getId())).build();
        final List<TrackDto> tracksDetailsDto = List.of(
                TrackDto.builder().id(1L).albumId(album.getId()).build(),
                TrackDto.builder().id(2L).albumId(album.getId()).build()
        );
        final CatalogueResult catalogueResult = CatalogueResult
                .builder()
                .artists(List.of(artist))
                .albums(List.of(album))
                .tracks(tracks)
                .build();
        final CatalogueResultDto catalogueResultDto = CatalogueResultDto
                .builder()
                .artists(List.of(artistDetailsDto))
                .albums(List.of(albumDetailsDto))
                .tracks(tracksDetailsDto)
                .build();

        when(catalogueService.findAllByGenreName(genre)).thenReturn(catalogueResult);
        when(catalogueMapper.toCatalogueResultDto(catalogueResult)).thenReturn(catalogueResultDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(catalogueController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/catalogue/genres/" + genre))
                .andExpect(status().isOk())
                .andReturn();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final CatalogueResultDto actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CatalogueResultDto.class
        );

        assertEquals(1, actualResponse.getArtists().size());
        assertTrue(actualResponse.getArtists().contains(artistDetailsDto));
        assertEquals(1, actualResponse.getAlbums().size());
        assertTrue(actualResponse.getAlbums().contains(albumDetailsDto));
        assertEquals(tracksDetailsDto.size(), actualResponse.getTracks().size());
        assertTrue(actualResponse.getTracks().containsAll(tracksDetailsDto));
        verify(catalogueService, times(1)).findAllByGenreName(anyString());
        verify(catalogueMapper, times(1)).toCatalogueResultDto(any());
    }
}
