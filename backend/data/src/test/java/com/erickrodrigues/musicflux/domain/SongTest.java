package com.erickrodrigues.musicflux.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    public void play() {
        final Song song = Song.builder().build();
        song.play();

        assertEquals(1, song.getNumberOfPlays());
    }

    @Test
    public void compareToWhenSongsHaveDifferentNumberOfPlays() {
        final Song song1 = Song.builder().numberOfPlays(7600L).build();
        final Song song2 = Song.builder().numberOfPlays(3600L).build();

        assertTrue(song1.compareTo(song2) < 0);
    }

    @Test
    public void compareToWhenSongsHaveSameNumberOfPlays() {
        final Song song1 = Song.builder()
                .title("World In My Eyes")
                .numberOfPlays(3600L)
                .build();
        final Song song2 = Song.builder()
                .title("Behind The Wheel")
                .numberOfPlays(3600L)
                .build();

        assertTrue(song2.compareTo(song1) < 0);
    }
}
