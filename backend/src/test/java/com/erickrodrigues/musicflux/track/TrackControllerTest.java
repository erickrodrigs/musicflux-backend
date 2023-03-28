package com.erickrodrigues.musicflux.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTest {

    @Mock
    private TrackService trackService;

    @Mock
    private TrackMapper trackMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrackController trackController;

    @Test
    public void shouldPlayATrack() throws Exception {
        final long userId = 1L, trackId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
        when(trackService.play(userId, trackId)).thenReturn(Track.builder()
                .title("My track")
                .build()
        );
        when(restTemplate.getForObject(anyString(), any())).thenReturn(new byte[] { (byte)0xe0 });

        mockMvc.perform(get("/users/me/tracks/" + trackId)
                        .requestAttr("userId", userId))
                .andExpect(status().isOk());

        verify(trackService, times(1)).play(anyLong(), anyLong());
    }

    @Test
    public void findAllByAlbumId() throws Exception {
        final Long albumId = 1L;
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
                        .albumId(albumId)
                        .build(),
                TrackDto.builder()
                        .id(2L)
                        .title("Love")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .albumId(albumId)
                        .build()
        );

        when(trackService.findAllByAlbumId(albumId)).thenReturn(tracks);
        when(trackMapper.toListOfTrackDetailsDto(tracks)).thenReturn(tracksDetailsDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/albums/" + albumId + "/tracks"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<TrackDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TrackDto.class)
        );

        assertEquals(tracksDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(tracksDetailsDto));
        verify(trackService, times(1)).findAllByAlbumId(anyLong());
        verify(trackMapper, times(1)).toListOfTrackDetailsDto(anyList());
    }

    @Test
    public void findMostPlayedTracksByArtistId() throws Exception {
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

        when(trackService.findMostPlayedTracksByArtistId(artistId)).thenReturn(tracks);
        when(trackMapper.toListOfTrackDetailsDto(tracks)).thenReturn(tracksDetailsDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/artists/" + artistId + "/most_played_tracks"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<TrackDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TrackDto.class)
        );

        assertEquals(tracksDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(tracksDetailsDto));
        verify(trackService, times(1)).findMostPlayedTracksByArtistId(anyLong());
        verify(trackMapper, times(1)).toListOfTrackDetailsDto(anyList());
    }
}
