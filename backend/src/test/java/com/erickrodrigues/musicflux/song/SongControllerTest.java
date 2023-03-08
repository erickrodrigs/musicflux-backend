package com.erickrodrigues.musicflux.song;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SongControllerTest {

    @Mock
    private SongService songService;

    @Mock
    private SongMapper songMapper;

    @InjectMocks
    private SongController songController;

    @Test
    public void playSong() throws Exception {
        final long userId = 1L, songId = 1L;
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(songController).build();

        mockMvc.perform(put("/users/me/songs/" + songId + "/play")
                        .requestAttr("userId", userId))
                .andExpect(status().isOk());

        verify(songService, times(1)).play(anyLong(), anyLong());
    }

    @Test
    public void findAllByAlbumId() throws Exception {
        final Long albumId = 1L;
        final List<Song> songs = List.of(
                Song.builder().id(1L).title("Sing This Song").build(),
                Song.builder().id(2L).title("Love").build()
        );
        final List<SongDetailsDto> songsDetailsDto = List.of(
                SongDetailsDto.builder()
                        .id(1L)
                        .title("Sing This Song")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .albumId(albumId)
                        .build(),
                SongDetailsDto.builder()
                        .id(2L)
                        .title("Love")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .albumId(albumId)
                        .build()
        );

        when(songService.findAllByAlbumId(albumId)).thenReturn(songs);
        when(songMapper.toSongDetailsDto(songs.get(0))).thenReturn(songsDetailsDto.get(0));
        when(songMapper.toSongDetailsDto(songs.get(1))).thenReturn(songsDetailsDto.get(1));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/albums/" + albumId + "/songs"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<SongDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, SongDetailsDto.class)
        );

        assertEquals(songsDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(songsDetailsDto));
        verify(songService, times(1)).findAllByAlbumId(anyLong());
        verify(songMapper, times(2)).toSongDetailsDto(any());
    }

    @Test
    public void findMostPlayedSongsByArtistId() throws Exception {
        final Long artistId = 1L;
        final List<Song> songs = List.of(
                Song.builder().id(1L).title("Sing This Song").build(),
                Song.builder().id(2L).title("Love").build()
        );
        final List<SongDetailsDto> songsDetailsDto = List.of(
                SongDetailsDto.builder()
                        .id(1L)
                        .title("Sing This Song")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .build(),
                SongDetailsDto.builder()
                        .id(2L)
                        .title("Love")
                        .length(60L)
                        .genres(List.of("Synth Pop"))
                        .build()
        );

        when(songService.findMostPlayedSongsByArtistId(artistId)).thenReturn(songs);
        when(songMapper.toSongDetailsDto(songs.get(0))).thenReturn(songsDetailsDto.get(0));
        when(songMapper.toSongDetailsDto(songs.get(1))).thenReturn(songsDetailsDto.get(1));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(songController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/artists/" + artistId + "/most_played_songs"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<SongDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, SongDetailsDto.class)
        );

        assertEquals(songsDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(songsDetailsDto));
        verify(songService, times(1)).findMostPlayedSongsByArtistId(anyLong());
        verify(songMapper, times(2)).toSongDetailsDto(any());
    }
}
