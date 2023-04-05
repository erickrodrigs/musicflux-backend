package com.erickrodrigues.musicflux.artist;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumDetailsDto;
import com.erickrodrigues.musicflux.album.AlbumMapper;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackDto;
import com.erickrodrigues.musicflux.track.TrackMapper;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerTest {

    private static final String WRONG_NUMBER_OF_ALBUMS = "Wrong number of albums";
    private static final String WRONG_NUMBER_OF_TRACKS = "Wrong number of tracks";
    private static final String ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_ALBUMS = "Actual list does not contain specified albums";
    private static final String ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS = "Actual list does not contain specified tracks";

    @Mock
    private ArtistService artistService;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private TrackMapper trackMapper;

    @InjectMocks
    private ArtistController artistController;

    @Test
    public void shouldReturnAllAlbumsFromAnArtist() throws Exception {
        // given
        final Long artistId = 1L;
        final List<Album> albums = List.of(
                Album.builder()
                        .id(1L)
                        .title("Black Celebration")
                        .releaseDate(LocalDate.parse("1986-03-17"))
                        .build(),
                Album.builder()
                        .id(2L)
                        .title("Music For The Masses")
                        .releaseDate(LocalDate.parse("1987-09-28"))
                        .build()
        );
        final List<AlbumDetailsDto> albumsDetailsDto = List.of(
                AlbumDetailsDto.builder()
                        .id(1L)
                        .title("Black Celebration")
                        .releaseDate(LocalDate.parse("1986-03-17"))
                        .artists(List.of(ArtistDetailsDto.builder().id(artistId).build()))
                        .build(),
                AlbumDetailsDto.builder()
                        .id(2L)
                        .title("Music For The Masses")
                        .releaseDate(LocalDate.parse("1987-09-28"))
                        .artists(List.of(ArtistDetailsDto.builder().id(artistId).build()))
                        .build()
        );
        when(artistService.getArtistAlbums(artistId)).thenReturn(albums);
        when(albumMapper.toListOfAlbumDetailsDto(albums)).thenReturn(albumsDetailsDto);

        // when
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/artists/" + artistId + "/albums"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        final List<AlbumDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, AlbumDetailsDto.class)
        );

        // then
        assertEquals(albumsDetailsDto.size(), actualResult.size(), WRONG_NUMBER_OF_ALBUMS);
        assertTrue(actualResult.containsAll(albumsDetailsDto), ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_ALBUMS);
        verify(artistService, times(1)).getArtistAlbums(artistId);
        verify(albumMapper, times(1)).toListOfAlbumDetailsDto(albums);
    }

    @Test
    public void shouldReturnTopTracksByAnArtist() throws Exception {
        // given
        final Long artistId = 1L;
        final List<Track> tracks = List.of(
                Track.builder().id(1L).title("Sing This Track").build(),
                Track.builder().id(2L).title("Love").build()
        );
        final List<TrackDto> tracksDetailsDto = List.of(
                TrackDto.builder()
                        .id(1L)
                        .title("Sing This Track")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .build(),
                TrackDto.builder()
                        .id(2L)
                        .title("Love")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .build()
        );
        when(artistService.getTopTracks(artistId)).thenReturn(tracks);
        when(trackMapper.toListOfTrackDetailsDto(tracks)).thenReturn(tracksDetailsDto);

        // when
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/artists/" + artistId + "/top-tracks"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<TrackDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TrackDto.class)
        );

        // then
        assertEquals(tracksDetailsDto.size(), actualResult.size(), WRONG_NUMBER_OF_TRACKS);
        assertTrue(actualResult.containsAll(tracksDetailsDto), ACTUAL_LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS);
        verify(artistService, times(1)).getTopTracks(artistId);
        verify(trackMapper, times(1)).toListOfTrackDetailsDto(tracks);
    }
}
