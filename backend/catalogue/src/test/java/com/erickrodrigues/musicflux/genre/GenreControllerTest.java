package com.erickrodrigues.musicflux.genre;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreController genreController;

    @Test
    public void shouldReturnAllGenres() throws Exception {
        // given
        final List<Genre> genres = List.of(
                Genre.builder().id(1L).name("Hip-Hop").build(),
                Genre.builder().id(2L).name("Pop").build()
        );
        final List<GenreDto> genreDtoList = List.of(
                GenreDto.builder().id(1L).name("Hip-Hop").build(),
                GenreDto.builder().id(2L).name("Pop").build()
        );
        when(genreService.findAll()).thenReturn(genres);
        when(genreMapper.toListOfGenreDto(genres)).thenReturn(genreDtoList);

        // when
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
        final MvcResult mvcResult = mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andReturn();
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<GenreDto> actualResult = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, GenreDto.class)
        );

        // then
        assertNotNull(actualResult);
        assertEquals(genreDtoList.size(), actualResult.size());
        assertTrue(actualResult.containsAll(genreDtoList));
    }
}
