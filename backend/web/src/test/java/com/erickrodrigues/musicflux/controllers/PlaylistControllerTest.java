package com.erickrodrigues.musicflux.controllers;

import com.erickrodrigues.musicflux.domain.Playlist;
import com.erickrodrigues.musicflux.dtos.AddSongDto;
import com.erickrodrigues.musicflux.dtos.CreatePlaylistDto;
import com.erickrodrigues.musicflux.dtos.PlaylistDetailsDto;
import com.erickrodrigues.musicflux.mappers.PlaylistMapper;
import com.erickrodrigues.musicflux.services.PlaylistService;
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
public class PlaylistControllerTest {

    @Mock
    private PlaylistService playlistService;

    @Mock
    private PlaylistMapper playlistMapper;

    @InjectMocks
    private PlaylistController playlistController;

    @Test
    public void createPlaylist() throws Exception {
        final String playlistName = "GREATEST ONES";
        final Long profileId = 1L;
        final CreatePlaylistDto createPlaylistDto = CreatePlaylistDto.builder().name(playlistName).build();
        final Playlist playlist = Playlist.builder().id(1L).name(playlistName).build();
        final PlaylistDetailsDto playlistDetailsDto = PlaylistDetailsDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .profileId(profileId)
                .build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(createPlaylistDto);

        when(playlistService.create(profileId, playlistName)).thenReturn(playlist);
        when(playlistMapper.toPlaylistDetailsDto(playlist)).thenReturn(playlistDetailsDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playlistController).build();
        final MvcResult mvcResult = mockMvc.perform(post("/profiles/" + profileId + "/playlists")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        final PlaylistDetailsDto actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                PlaylistDetailsDto.class
        );

        assertEquals(playlistDetailsDto.getId(), actualResult.getId());
        assertEquals(playlistDetailsDto.getName(), actualResult.getName());
        assertEquals(playlistDetailsDto.getProfileId(), actualResult.getProfileId());
        verify(playlistService, times(1)).create(anyLong(), anyString());
        verify(playlistMapper, times(1)).toPlaylistDetailsDto(any());
    }

    @Test
    public void findAllByProfileId() throws Exception {
        final List<Playlist> playlists = List.of(
                Playlist.builder().id(1L).name("GREATEST ONES").build(),
                Playlist.builder().id(2L).name("HEAVY METAL").build()
        );
        final List<PlaylistDetailsDto> playlistsDetailsDto = List.of(
                PlaylistDetailsDto.builder().id(1L).name("GREATEST ONES").profileId(1L).build(),
                PlaylistDetailsDto.builder().id(2L).name("HEAVY METAL").profileId(1L).build()
        );
        final Long profileId = 1L;

        when(playlistService.findAllByProfileId(profileId)).thenReturn(playlists);
        when(playlistMapper.toPlaylistDetailsDto(playlists.get(0))).thenReturn(playlistsDetailsDto.get(0));
        when(playlistMapper.toPlaylistDetailsDto(playlists.get(1))).thenReturn(playlistsDetailsDto.get(1));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playlistController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/profiles/" + profileId + "/playlists"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<PlaylistDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, PlaylistDetailsDto.class)
        );

        assertEquals(playlistsDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(playlistsDetailsDto));
        verify(playlistService, times(1)).findAllByProfileId(anyLong());
        verify(playlistMapper, times(2)).toPlaylistDetailsDto(any());
    }

    @Test
    public void addSong() throws Exception {
        final Long profileId = 1L, songId = 1L;
        final AddSongDto addSongDto = AddSongDto.builder().songId(songId).build();
        final Playlist playlist = Playlist.builder().id(1L).name("playlist").build();
        final PlaylistDetailsDto playlistDetailsDto = PlaylistDetailsDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .profileId(profileId)
                .build();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(addSongDto);

        when(playlistService.addSong(profileId, playlist.getId(), songId)).thenReturn(playlist);
        when(playlistMapper.toPlaylistDetailsDto(playlist)).thenReturn(playlistDetailsDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playlistController).build();
        final MvcResult mvcResult = mockMvc.perform(put("/profiles/" + profileId + "/playlists/" + playlist.getId() + "/songs")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();
        final PlaylistDetailsDto actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                PlaylistDetailsDto.class
        );

        assertEquals(playlistDetailsDto.getId(), actualResult.getId());
        assertEquals(playlistDetailsDto.getName(), actualResult.getName());
        assertEquals(playlistDetailsDto.getProfileId(), actualResult.getProfileId());
        verify(playlistService, times(1)).addSong(anyLong(), anyLong(), anyLong());
        verify(playlistMapper, times(1)).toPlaylistDetailsDto(any());
    }

    @Test
    public void removeSong() throws Exception {
        final Long profileId = 1L, playlistId = 1L, songId = 1L;
        final Playlist playlist = Playlist.builder().id(1L).name("playlist").build();
        final PlaylistDetailsDto playlistDetailsDto = PlaylistDetailsDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .profileId(profileId)
                .build();

        when(playlistService.removeSong(profileId, playlistId, songId)).thenReturn(playlist);
        when(playlistMapper.toPlaylistDetailsDto(playlist)).thenReturn(playlistDetailsDto);

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playlistController).build();
        final MvcResult mvcResult = mockMvc.perform(delete("/profiles/" + profileId + "/playlists/" + playlistId + "/songs/" + songId))
                .andExpect(status().isOk())
                .andReturn();
        final PlaylistDetailsDto actualResult = new ObjectMapper().readValue(
                mvcResult.getResponse().getContentAsString(),
                PlaylistDetailsDto.class
        );

        assertEquals(playlistDetailsDto.getId(), actualResult.getId());
        assertEquals(playlistDetailsDto.getName(), actualResult.getName());
        assertEquals(playlistDetailsDto.getProfileId(), actualResult.getProfileId());
        verify(playlistService, times(1)).removeSong(anyLong(), anyLong(), anyLong());
        verify(playlistMapper, times(1)).toPlaylistDetailsDto(any());
    }

    @Test
    public void deletePlaylist() throws Exception {
        final long profileId = 1L, playlistId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(playlistController).build();

        mockMvc.perform(delete("/profiles/" + profileId + "/playlists/" + playlistId))
                .andExpect(status().isOk());

        verify(playlistService, times(1)).deleteById(anyLong(), anyLong());
    }
}
