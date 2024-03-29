package com.erickrodrigues.musicflux.recently_played;

import com.erickrodrigues.musicflux.album.Album;
import com.erickrodrigues.musicflux.album.AlbumDto;
import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistDto;
import com.erickrodrigues.musicflux.track.Track;
import com.erickrodrigues.musicflux.track.TrackDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RecentlyPlayedControllerTest {

    @Mock
    private RecentlyPlayedService recentlyPlayedService;

    @Mock
    private RecentlyPlayedMapper recentlyPlayedMapper;

    @InjectMocks
    private RecentlyPlayedController recentlyPlayedController;

    @Test
    public void findAllByUserId() throws Exception {
        final Long userId = 1L;
        final List<RecentlyPlayed> recentlyPlayedTracks = List.of(
                RecentlyPlayed.builder().id(1L).track(
                        Track.builder()
                                .id(1L)
                                .title("")
                                .album(Album.builder().title("").artists(List.of(Artist.builder().name("").build())).build())
                                .build()).build(),
                RecentlyPlayed.builder().id(2L).track(
                        Track.builder()
                                .id(2L)
                                .title("")
                                .album(Album.builder().title("").artists(List.of(Artist.builder().name("").build())).build())
                                .build()).build()
        );
        final List<RecentlyPlayedDetailsDto> recentlyPlayedDetailsDtos = List.of(
                RecentlyPlayedDetailsDto.builder()
                        .id(1L)
                        .track(TrackDto.builder().build())
                        .album(AlbumDto.builder().build())
                        .artists(List.of(ArtistDto.builder().build()))
                        .createdAt(LocalDateTime.now())
                        .build(),
                RecentlyPlayedDetailsDto.builder()
                        .id(2L)
                        .track(TrackDto.builder().build())
                        .album(AlbumDto.builder().build())
                        .artists(List.of(ArtistDto.builder().build()))
                        .createdAt(LocalDateTime.now().plusDays(1L))
                        .build()
        );
        final Page<RecentlyPlayed> recentlyPlayedTracksPage = new PageImpl<>(recentlyPlayedTracks);

        when(recentlyPlayedService.findAllByUserId(any(), eq(userId))).thenReturn(recentlyPlayedTracksPage);
        when(recentlyPlayedMapper.toRecentlyPlayedDetailsDto(recentlyPlayedTracks.get(0))).thenReturn(recentlyPlayedDetailsDtos.get(0));
        when(recentlyPlayedMapper.toRecentlyPlayedDetailsDto(recentlyPlayedTracks.get(1))).thenReturn(recentlyPlayedDetailsDtos.get(1));

        final JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final JSONArray listJson = (JSONArray) jsonParser.parse(objectMapper.writeValueAsString(recentlyPlayedDetailsDtos));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recentlyPlayedController).build();
        mockMvc.perform(get("/me/recently-played")
                        .requestAttr("userId", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(Matchers.containsInAnyOrder(listJson.toArray())))
                .andReturn();

        verify(recentlyPlayedService, times(1)).findAllByUserId(any(), anyLong());
        verify(recentlyPlayedMapper, times(2)).toRecentlyPlayedDetailsDto(any());
    }
}
