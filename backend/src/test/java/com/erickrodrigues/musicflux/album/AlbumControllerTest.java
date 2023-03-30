package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackDto;
import com.erickrodrigues.musicflux.track.TrackMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AlbumControllerTest {

    private static final String WRONG_NUMBER_OF_TRACKS = "Wrong number of tracks";
    private static final String LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS = "Actual list does not contain specified tracks";

    @Mock
    private AlbumService albumService;

    @Mock
    private TrackMapper trackMapper;

    @InjectMocks
    private AlbumController albumController;

    @Test
    public void shouldReturnAllTracksFromAnAlbum() throws Exception {
        // given
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
        when(albumService.getAlbumTracks(albumId)).thenReturn(tracks);
        when(trackMapper.toListOfTrackDetailsDto(tracks)).thenReturn(tracksDetailsDto);

        // when
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/albums/" + albumId + "/tracks"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<TrackDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TrackDto.class)
        );

        // then
        assertEquals(tracksDetailsDto.size(), actualResult.size(), WRONG_NUMBER_OF_TRACKS);
        assertTrue(actualResult.containsAll(tracksDetailsDto), LIST_DOES_NOT_CONTAIN_SPECIFIED_TRACKS);
        verify(albumService, times(1)).getAlbumTracks(albumId);
        verify(trackMapper, times(1)).toListOfTrackDetailsDto(tracks);
    }
}
