package com.erickrodrigues.musicflux.catalogue;

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
public class CatalogueControllerTest {

    @Mock
    private CatalogueService catalogueService;

    @Mock
    private CatalogueMapper catalogueMapper;

    @InjectMocks
    private CatalogueController catalogueController;

    @Test
    public void shouldSearchAndReturnProperResult() throws Exception {
        // given
        final SearchableType type = SearchableType.ARTIST;
        final String text = "dark";
        final CatalogueResult catalogueResult = CatalogueResult
                .builder()
                .artists(List.of(
                        Artist.builder().id(1L).name("Dark Angel").build()
                ))
                .build();
        final CatalogueResultDto catalogueResultDto = CatalogueResultDto
                .builder()
                .artists(List.of(
                        ArtistDetailsDto.builder().id(1L).name("Dark Angel").build()
                ))
                .build();
        when(catalogueService.findAllByTypeAndText(type, text)).thenReturn(catalogueResult);
        when(catalogueMapper.toCatalogueResultDto(catalogueResult)).thenReturn(catalogueResultDto);

        // when
        final MultiValueMap<String, String> params;
        final MockMvc mockMvc;
        final MvcResult mvcResult;
        final ObjectMapper objectMapper;
        final CatalogueResultDto actualResponse;
        params = new LinkedMultiValueMap<>();
        params.add("type", type.name());
        params.add("q", text);

        System.out.println(type.getType());
        mockMvc = MockMvcBuilders.standaloneSetup(catalogueController).build();
        mvcResult = mockMvc.perform(get("/search")
                        .params(params))
                .andExpect(status().isOk())
                .andReturn();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        actualResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CatalogueResultDto.class
        );

        // then
        assertEquals(catalogueResultDto.getArtists().size(), actualResponse.getArtists().size());
        assertTrue(actualResponse.getArtists().containsAll(catalogueResultDto.getArtists()));
        verify(catalogueService, times(1)).findAllByTypeAndText(type, text);
        verify(catalogueMapper, times(1)).toCatalogueResultDto(catalogueResult);
    }
}
