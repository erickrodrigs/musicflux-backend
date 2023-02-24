package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Artist;
import com.erickrodrigues.musicflux.repositories.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceImplTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistServiceImpl artistService;

    @Test
    public void findAllByName() {
        String name = "iron";
        Set<Artist> artists = Set.of(
                Artist.builder().id(1L).name("Iron Maiden").build(),
                Artist.builder().id(2L).name("Iron Savior").build()
        );

        when(artistRepository.findAllByNameContainingIgnoreCase(name)).thenReturn(artists);

        assertEquals(2, artistService.findAllByNameContainingIgnoreCase(name).size());
        verify(artistRepository, times(1)).findAllByNameContainingIgnoreCase(anyString());
    }
}
