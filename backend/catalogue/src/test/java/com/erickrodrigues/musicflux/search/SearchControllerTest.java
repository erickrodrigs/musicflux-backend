package com.erickrodrigues.musicflux.search;

import com.erickrodrigues.musicflux.artist.Artist;
import com.erickrodrigues.musicflux.artist.ArtistDetailsDto;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @Mock
    private SearchMapper searchMapper;

    @InjectMocks
    private SearchController searchController;

    @Test
    public void shouldSearchAndReturnProperResult() throws Exception {
        // given
        final SearchableType type = SearchableType.ARTIST;
        final String text = "dark";
        final SearchResult searchResult = SearchResult
                .builder()
                .artists(List.of(
                        Artist.builder().id(1L).name("Dark Angel").build()
                ))
                .build();
        final SearchResultDto searchResultDto = SearchResultDto
                .builder()
                .artists(List.of(
                        ArtistDetailsDto.builder().id(1L).name("Dark Angel").build()
                ))
                .build();
        when(searchService.findAllByTypeAndText(type, text)).thenReturn(searchResult);
        when(searchMapper.toSearchResultDto(searchResult)).thenReturn(searchResultDto);

        // when
        final MultiValueMap<String, String> params;
        final MockMvc mockMvc;
        final MvcResult mvcResult;
        final ObjectMapper objectMapper;
        final SearchResultDto actualResponse;
        params = new LinkedMultiValueMap<>();
        params.add("type", type.name());
        params.add("q", text);

        System.out.println(type.getType());
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
        mvcResult = mockMvc.perform(get("/search")
                        .params(params))
                .andExpect(status().isOk())
                .andReturn();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                SearchResultDto.class
        );

        // then
        assertEquals(searchResultDto.getArtists().size(), actualResponse.getArtists().size());
        assertTrue(actualResponse.getArtists().containsAll(searchResultDto.getArtists()));
        verify(searchService, times(1)).findAllByTypeAndText(type, text);
        verify(searchMapper, times(1)).toSearchResultDto(searchResult);
    }
}
