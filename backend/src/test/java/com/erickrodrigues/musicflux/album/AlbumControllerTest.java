package com.erickrodrigues.musicflux.album;

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
public class AlbumControllerTest {

    @Mock
    private AlbumService albumService;

    @Mock
    private AlbumMapper albumMapper;

    @InjectMocks
    private AlbumController albumController;

    @Test
    public void findAllByArtistId() throws Exception {
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
                        .artistsIds(List.of(artistId))
                        .build(),
                AlbumDetailsDto.builder()
                        .id(2L)
                        .title("Music For The Masses")
                        .releaseDate(LocalDate.parse("1987-09-28"))
                        .artistsIds(List.of(artistId))
                        .build()
        );

        when(albumService.findAllByArtistId(artistId)).thenReturn(albums);
        when(albumMapper.toAlbumDetailsDto(albums.get(0))).thenReturn(albumsDetailsDto.get(0));
        when(albumMapper.toAlbumDetailsDto(albums.get(1))).thenReturn(albumsDetailsDto.get(1));

        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/artists/" + artistId + "/albums"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        final List<AlbumDetailsDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, AlbumDetailsDto.class)
        );

        assertEquals(albumsDetailsDto.size(), actualResult.size());
        assertTrue(actualResult.containsAll(albumsDetailsDto));
        verify(albumService, times(1)).findAllByArtistId(anyLong());
        verify(albumMapper, times(2)).toAlbumDetailsDto(any());
    }
}
