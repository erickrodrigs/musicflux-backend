package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.track.TrackDetailsDto;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

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
    public void likeTrack() throws Exception {
        final Long userId = 1L, trackId = 1L;
        final Favorite favorite = Favorite.builder().id(1L).build();
        final FavoriteDetailsDto favoriteDetailsDto = FavoriteDetailsDto.builder()
                .id(1L)
                .track(TrackDetailsDto.builder().id(1L).build())
                .userId(userId)
                .build();
        final CreateFavoriteDto createFavoriteDto = CreateFavoriteDto.builder().trackId(trackId).build();

        when(favoriteService.likeTrack(userId, trackId)).thenReturn(favorite);
        when(favoriteMapper.toFavoriteDetailsDto(favorite)).thenReturn(favoriteDetailsDto);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(createFavoriteDto);
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
        final MvcResult mvcResult = mockMvc.perform(post("/me/favorites")
                        .requestAttr("userId", userId)
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
        assertEquals(favoriteDetailsDto.getTrack().getId(), actualResult.getTrack().getId());
        assertEquals(favoriteDetailsDto.getUserId(), actualResult.getUserId());
        verify(favoriteService, times(1)).likeTrack(anyLong(), anyLong());
        verify(favoriteMapper, times(1)).toFavoriteDetailsDto(any());
    }

    @Test
    public void dislikeTrack() throws Exception {
        final long userId = 1L, favoriteId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();

        mockMvc.perform(delete("/me/favorites/" + favoriteId)
                        .requestAttr("userId", userId))
                .andExpect(status().isOk());

        verify(favoriteService, times(1)).dislikeTrack(anyLong(), anyLong());
    }

    @Test
    public void findAllByUserId() throws Exception {
        final Long userId = 1L;
        final List<Favorite> favorites = List.of(
                Favorite.builder().id(1L).build(),
                Favorite.builder().id(2L).build()
        );
        final List<FavoriteDetailsDto> favoritesDetailsDto = List.of(
                FavoriteDetailsDto.builder()
                        .id(1L)
                        .track(TrackDetailsDto.builder().id(1L).build())
                        .userId(userId)
                        .build(),
                FavoriteDetailsDto.builder()
                        .id(2L)
                        .track(TrackDetailsDto.builder().id(2L).build())
                        .userId(userId)
                        .build()
        );

        when(favoriteService.findAllByUserId(userId)).thenReturn(favorites);
        when(favoriteMapper.toListOfFavoriteDetailsDto(favorites)).thenReturn(favoritesDetailsDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/me/favorites")
                        .requestAttr("userId", userId))
                .andExpect(status().isOk())
                .andReturn();

        final ObjectMapper objectMapper = new ObjectMapper();
        final List<FavoriteDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, FavoriteDetailsDto.class)
        );

        assertEquals(favoritesDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(favoritesDetailsDto));
        verify(favoriteService, times(1)).findAllByUserId(anyLong());
        verify(favoriteMapper, times(1)).toListOfFavoriteDetailsDto(anyList());
    }

    @Test
    public void shouldReturnIfTracksAreLikedOrNot() throws Exception {
        // given
        final Long userId = 1L;
        final List<Long> tracksIds = List.of(1L, 2L);
        final Map<Long, Boolean> tracksToLiked = Map.of(
                tracksIds.get(0), true,
                tracksIds.get(1), false
        );
        when(favoriteService.checkWhetherTracksAreLiked(userId, tracksIds)).thenReturn(tracksToLiked);

        // when
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("tracksIds", tracksIds.stream().map(Object::toString).toList());
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(favoriteController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/me/favorites/check-liked")
                .requestAttr("userId", userId)
                .params(params))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<Long, Boolean> actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructMapType(Map.class, Long.class, Boolean.class)
        );

        // then
        assertEquals(tracksToLiked.get(tracksIds.get(0)), actualResponse.get(tracksIds.get(0)));
        assertEquals(tracksToLiked.get(tracksIds.get(1)), actualResponse.get(tracksIds.get(1)));
        verify(favoriteService, times(1)).checkWhetherTracksAreLiked(userId, tracksIds);
    }
}
