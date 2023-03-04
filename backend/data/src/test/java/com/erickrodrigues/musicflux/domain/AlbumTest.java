package com.erickrodrigues.musicflux.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AlbumTest {

    @Test
    public void compareToWhenReleaseDateIsPresent() {
        final Album album1 = Album.builder()
                .id(1L)
                .title("Music For The Masses")
                .releaseDate(LocalDate.parse("1987-09-28"))
                .build();
        final Album album2 = Album.builder()
                .id(2L)
                .title("Black Celebration")
                .releaseDate(LocalDate.parse("1986-03-17"))
                .build();

        assertTrue(album2.compareTo(album1) < 0);
    }

    @Test
    public void compareToWhenReleaseDateIsNotPresent() {
        final Album album1 = Album.builder()
                .id(1L)
                .title("Music For The Masses")
                .build();
        final Album album2 = Album.builder()
                .id(2L)
                .title("Black Celebration")
                .build();

        assertTrue(album1.compareTo(album2) < 0);
    }
}
