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
}
