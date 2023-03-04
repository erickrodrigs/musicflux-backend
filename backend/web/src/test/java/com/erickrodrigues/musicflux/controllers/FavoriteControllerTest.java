package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.domain.Favorite;
import com.erickrodrigues.musicflux.dtos.CreateFavoriteDto;
import com.erickrodrigues.musicflux.dtos.FavoriteDetailsDto;
import com.erickrodrigues.musicflux.dtos.SongDetailsDto;
import com.erickrodrigues.musicflux.mappers.FavoriteMapper;
import com.erickrodrigues.musicflux.services.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest {

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private FavoriteMapper favoriteMapper;

    @InjectMocks
    private FavoriteController favoriteController;

    @Test
    public void likeSong() throws Exception {
        final Long profileId = 1L, songId = 1L;
        final Favorite favorite = Favorite.builder().id(1L).build();
        final FavoriteDetailsDto favoriteDetailsDto = FavoriteDetailsDto.builder()
                .id(1L)
                .song(SongDetailsDto.builder().id(1L).build())
                .profileId(profileId)
                .build();
        final CreateFavoriteDto createFavoriteDto = CreateFavoriteDto.builder().songId(songId).build();

        when(favoriteService.likeSong(profileId, songId)).thenReturn(favorite);
        when(favoriteMapper.toFavoriteDetailsDto(favorite)).thenReturn(favoriteDetailsDto);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(createFavoriteDto);
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
        final MvcResult mvcResult = mockMvc.perform(post("/profiles/" + profileId + "/favorites")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        final FavoriteDetailsDto actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                FavoriteDetailsDto.class
        );

        assertEquals(favoriteDetailsDto.getId(), actualResult.getId());
        assertEquals(favoriteDetailsDto.getSong().getId(), actualResult.getSong().getId());
        assertEquals(favoriteDetailsDto.getProfileId(), actualResult.getProfileId());
        verify(favoriteService, times(1)).likeSong(anyLong(), anyLong());
        verify(favoriteMapper, times(1)).toFavoriteDetailsDto(any());
    }

    @Test
    public void dislikeSong() throws Exception {
        final long profileId = 1L, favoriteId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();

        mockMvc.perform(delete("/profiles/" + profileId + "/favorites/" + favoriteId))
                .andExpect(status().isOk());

        verify(favoriteService, times(1)).dislikeSong(anyLong(), anyLong());
    }

    @Test
    public void findAllByProfileId() throws Exception {
        final Long profileId = 1L;
        final List<Favorite> favorites = List.of(
                Favorite.builder().id(1L).build(),
                Favorite.builder().id(2L).build()
        );
        final List<FavoriteDetailsDto> favoritesDetailsDto = List.of(
                FavoriteDetailsDto.builder()
                        .id(1L)
                        .song(SongDetailsDto.builder().id(1L).build())
                        .profileId(profileId)
                        .build(),
                FavoriteDetailsDto.builder()
                        .id(2L)
                        .song(SongDetailsDto.builder().id(2L).build())
                        .profileId(profileId)
                        .build()
        );

        when(favoriteService.findAllByProfileId(profileId)).thenReturn(favorites);
        when(favoriteMapper.toFavoriteDetailsDto(favorites.get(0))).thenReturn(favoritesDetailsDto.get(0));
        when(favoriteMapper.toFavoriteDetailsDto(favorites.get(1))).thenReturn(favoritesDetailsDto.get(1));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/profiles/" + profileId + "/favorites"))
                .andExpect(status().isOk())
                .andReturn();

        final ObjectMapper objectMapper = new ObjectMapper();
        final List<FavoriteDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, FavoriteDetailsDto.class)
        );

        assertEquals(favoritesDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(favoritesDetailsDto));
        verify(favoriteService, times(1)).findAllByProfileId(anyLong());
        verify(favoriteMapper, times(2)).toFavoriteDetailsDto(any());
    }
}
