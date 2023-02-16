package com.erickrodrigues.musicflux.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {

    @Test
    public void play() {
        Song song = Song.builder().build();
        song.play();

        assertEquals(1, song.getNumberOfPlays());
    }

    @Test
    public void compareTo() {
        Song song1 = Song.builder().numberOfPlays(7600L).build();
        Song song2 = Song.builder().numberOfPlays(3600L).build();

        assertTrue(song1.compareTo(song2) < 0);
    }
}
