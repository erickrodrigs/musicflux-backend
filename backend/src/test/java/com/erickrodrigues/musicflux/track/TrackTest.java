package com.erickrodrigues.musicflux.track;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrackTest {

    @Test
    public void play() {
        final Track track = Track.builder().build();
        track.play();

        assertEquals(1, track.getNumberOfPlays());
    }

    @Test
    public void compareToWhenTracksHaveDifferentNumberOfPlays() {
        final Track track1 = Track.builder().numberOfPlays(7600L).build();
        final Track track2 = Track.builder().numberOfPlays(3600L).build();

        assertTrue(track1.compareTo(track2) < 0);
    }

    @Test
    public void compareToWhenTracksHaveSameNumberOfPlays() {
        final Track track1 = Track.builder()
                .title("World In My Eyes")
                .numberOfPlays(3600L)
                .build();
        final Track track2 = Track.builder()
                .title("Behind The Wheel")
                .numberOfPlays(3600L)
                .build();

        assertTrue(track2.compareTo(track1) < 0);
    }
}
