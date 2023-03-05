package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.domain.RecentlyPlayed;
import com.erickrodrigues.musicflux.domain.Song;
import com.erickrodrigues.musicflux.dtos.RecentlyPlayedDetailsDto;
import com.erickrodrigues.musicflux.dtos.SongDetailsDto;
import com.erickrodrigues.musicflux.mappers.RecentlyPlayedMapper;
import com.erickrodrigues.musicflux.services.RecentlyPlayedService;
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
    public void findAllByProfileId() throws Exception {
        final Long profileId = 1L;
        final List<RecentlyPlayed> recentlyPlayedSongs = List.of(
                RecentlyPlayed.builder().id(1L).song(Song.builder().id(1L).build()).build(),
                RecentlyPlayed.builder().id(2L).song(Song.builder().id(2L).build()).build()
        );
        final List<RecentlyPlayedDetailsDto> recentlyPlayedDetailsDtos = List.of(
                RecentlyPlayedDetailsDto.builder()
                        .id(1L)
                        .song(SongDetailsDto.builder().id(1L).build())
                        .profileId(profileId)
                        .createdAt(LocalDateTime.now())
                        .build(),
                RecentlyPlayedDetailsDto.builder()
                        .id(2L)
                        .song(SongDetailsDto.builder().id(2L).build())
                        .profileId(profileId)
                        .createdAt(LocalDateTime.now().plusDays(1L))
                        .build()
        );
        final Page<RecentlyPlayed> recentlyPlayedSongsPage = new PageImpl<>(recentlyPlayedSongs);

        when(recentlyPlayedService.findAllByProfileId(any(), eq(profileId))).thenReturn(recentlyPlayedSongsPage);
        when(recentlyPlayedMapper.toRecentlyPlayedDetailsDto(recentlyPlayedSongs.get(0))).thenReturn(recentlyPlayedDetailsDtos.get(0));
        when(recentlyPlayedMapper.toRecentlyPlayedDetailsDto(recentlyPlayedSongs.get(1))).thenReturn(recentlyPlayedDetailsDtos.get(1));

        final JSONParser jsonParser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final JSONArray listJson = (JSONArray) jsonParser.parse(objectMapper.writeValueAsString(recentlyPlayedDetailsDtos));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recentlyPlayedController).build();
        mockMvc.perform(get("/profiles/" + profileId + "/recently_played")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(Matchers.containsInAnyOrder(listJson.toArray())))
                .andReturn();

        verify(recentlyPlayedService, times(1)).findAllByProfileId(any(), anyLong());
        verify(recentlyPlayedMapper, times(2)).toRecentlyPlayedDetailsDto(any());
    }
}
