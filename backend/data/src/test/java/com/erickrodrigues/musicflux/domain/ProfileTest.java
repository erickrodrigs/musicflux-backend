package com.erickrodrigues.musicflux.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

    @Test
    public void addRecentlyListenedSong() {
        Profile profile = Profile.builder().id(1L).build();

        profile.addRecentlyListenedSong(Song.builder().id(1L).build());

        assertEquals(1, profile.getRecentlyListenedSongs().size());
    }
}
