package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Genre;
import com.erickrodrigues.musicflux.repositories.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    public void findAll() {
        List<Genre> genres = List.of(
                Genre.builder().id(1L).name("Rock").build(),
                Genre.builder().id(2L).name("Synth pop").build(),
                Genre.builder().id(3L).name("Dance").build(),
                Genre.builder().id(4L).name("Indie").build()
        );

        when(genreRepository.findAll()).thenReturn(genres);

        assertEquals(4, genreService.findAll().size());
        verify(genreRepository, times(1)).findAll();
    }
}
